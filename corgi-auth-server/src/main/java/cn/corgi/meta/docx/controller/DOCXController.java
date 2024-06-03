package cn.corgi.meta.docx.controller;

import cn.corgi.meta.docx.bean.DOCXBaseWrapper;
import cn.corgi.meta.docx.bean.HaiMingWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Slf4j
@RestController
@RequestMapping("api/docx")
public class DOCXController {

    @Operation(summary = "替换并生成docx文件")
    @RequestMapping("generate")
    public void generate(HttpServletRequest request, HttpServletResponse response, @RequestBody DOCXBaseWrapper docxBaseWrapper) {
//        URL resource = this.getClass().getClassLoader().getResource("templates/NNC1_BRN.docx");
        String templateName = "";
        if (Objects.equals(docxBaseWrapper.getLangType(), 0)) {
            templateName = HaiMingWrapper.EN_FILE_NAME;
        } else if (Objects.equals(docxBaseWrapper.getLangType(), 1)) {
            templateName = HaiMingWrapper.CN_FILE_NAME;
        }

        if (StringUtils.isEmpty(templateName)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        URL resource = this.getClass().getClassLoader().getResource("templates/" + templateName);

        if (null == resource) {
            log.error("缺少模板！name={}", templateName);
            throw new RuntimeException("缺少模板！name=" + templateName);
        }

        File file = new File(resource.getFile());
        File outFile = null;
        String extension = FilenameUtils.getExtension(file.getName());

        String outFileName = UUID.randomUUID().toString().substring(0, 6) + "-" + templateName;

        try {
            InputStream is = new FileInputStream(file);
            switch (extension) {
                case "doc":
//                    handlerByDocFile(is);
                    // 暂不处理doc
                    break;
                case "docx":
                    outFile = handlerByDocxFile(is, docxBaseWrapper, outFileName);
                    break;
                default:
                    throw new IllegalArgumentException("不能解析的文档类型，请输入正确的word文档类型的文件！");
            }
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        if (null != outFile) {
            try {
                FileInputStream fileInputStream = FileUtils.openInputStream(outFile);
                ServletOutputStream outputStream = response.getOutputStream();

                response.setContentType("application/octet-stream;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(outFileName, StandardCharsets.UTF_8));
                //读文件，写文件
                byte[] bytes = new byte[1024];
                int len=0;
                while ((len=fileInputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static File handlerByDocxFile(InputStream is, DOCXBaseWrapper docxBaseWrapper, String outFileName) throws IOException, InvalidFormatException {
        XWPFDocument xwpfDocument = new XWPFDocument(is);
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();

        List<String> paragraphData = docxBaseWrapper.getParagraphData();
        List<String> tableData = docxBaseWrapper.getTableData();

        int pPos = 0;
        int tPos = 0;

        // 处理段落
        for (XWPFParagraph paragraph : paragraphs) {
            pPos = replaceData(paragraphData, pPos, paragraph);
        }
        // 处理表格
        List<XWPFTable> tables = xwpfDocument.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (XWPFTableCell tableCell : tableCells) {
                    List<XWPFParagraph> para = tableCell.getParagraphs();
                    for (XWPFParagraph xwpfParagraph : para) {
                        tPos = replaceData(tableData, tPos, xwpfParagraph);
                    }
                }
            }
        }

        //生成新的word
        File file = new File(outFileName);
        FileOutputStream stream = new FileOutputStream(file);
        xwpfDocument.write(stream);
        stream.close();

        xwpfDocument.close();
        is.close();

        return file;
    }

    private static int replaceData(List<String> dataList, int pos, XWPFParagraph xwpfParagraph) {
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        for (XWPFRun run : runs) {
            if (matched(run.toString())) {
                String tData = "";
                if (!CollectionUtils.isEmpty(dataList) && pos < dataList.size()) {
                    tData = dataList.get(pos++);
                }
                run.setText(replaceText(run.toString(), tData), 0);
            }
        }
        return pos;
    }

    private static boolean matched(String string) {
        return string.matches("(\\{\\{.*}})");
    }

    public static String replaceText(String text, String replaceText) {
        if (text.matches("(\\{\\{.*}})")) {
            return text.replaceAll("(\\{\\{.*}})", replaceText);
        }
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }
}
