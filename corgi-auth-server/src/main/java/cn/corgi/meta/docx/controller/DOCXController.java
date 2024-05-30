package cn.corgi.meta.docx.controller;

import cn.corgi.meta.docx.bean.HaiMingWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.List;
import java.util.Objects;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@RestController
@RequestMapping("api/docx")
public class DOCXController {

    @Operation(summary = "替换并生成docx文件")
    @RequestMapping("generate")
    public void generate(HttpServletRequest request, HttpServletResponse response, @RequestBody HaiMingWrapper haiMingWrapper) {
//        URL resource = this.getClass().getClassLoader().getResource("templates/NNC1_BRN.docx");
        String templateName = "";
        if (Objects.equals(haiMingWrapper.getLangType(), 0)) {
            templateName = HaiMingWrapper.EN_FILE_NAME;
        } else if (Objects.equals(haiMingWrapper.getLangType(), 1)) {
            templateName = HaiMingWrapper.CN_FILE_NAME;
        }

        if (StringUtils.isEmpty(templateName)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        URL resource = this.getClass().getClassLoader().getResource("templates/" + templateName);
        File file = new File(resource.getFile());
        File outFile = null;
        String extension = FilenameUtils.getExtension(file.getName());
        try {
            InputStream is = new FileInputStream(file);
            switch (extension) {
                case "doc":
//                    handlerByDocFile(is);
                    // 暂不处理doc
                    break;
                case "docx":
                    outFile = handlerByDocxFile(is);
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

                //读文件，写文件
                byte[] bytes = new byte[1024];
                int len=0;
                while ((len=fileInputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }

                fileInputStream.close();

                outputStream.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static File handlerByDocxFile(InputStream is) throws IOException, InvalidFormatException {
        XWPFDocument xwpfDocument = new XWPFDocument(is);
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
        // 处理段落
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                System.out.print("=>" + run + "<=");
                if (matched(run.toString())) {
                    run.setText(replaceText(run.toString()), 0);
                }
            }
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
                        List<XWPFRun> runs = xwpfParagraph.getRuns();
                        for (XWPFRun run : runs) {
                            System.out.print("=>" + run + "<=");
                            if (matched(run.toString())) {
                                run.setText(replaceText(run.toString()), 0);
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }

        //生成新的word
        File file = new File("output.docx");
        FileOutputStream stream = new FileOutputStream(file);
        xwpfDocument.write(stream);
        stream.close();

        xwpfDocument.close();
        is.close();

        return file;
    }

    private static boolean matched(String string) {
        return string.matches("(\\{\\{.*}})");
    }

    public static String replaceText(String text) {
        if (text.matches("(\\{\\{.*}})")) {
            return text.replaceAll("(\\{\\{.*}})", "测试");
        }
        if (StringUtils.isEmpty(text)) {
            return "{{parameter}}";
        }
        return text;
    }
}
