package cn.corgi.meta.docx.controller;

import cn.corgi.meta.base.config.MediaConfig;
import cn.corgi.meta.docx.bean.DOCXBaseWrapper;
import cn.corgi.meta.docx.constant.ReplaceTypeEnum;
import cn.corgi.meta.docx.entity.DOCXGenerateRecord;
import cn.corgi.meta.docx.service.DOCXService;
import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/docx")
public class DOCXController {

    private final MediaConfig mediaConfig;
    private final DOCXService docxService;

    @Operation(summary = "替换并生成docx文件")
    @RequestMapping("generate")
    public DOCXGenerateRecord generate(HttpServletRequest request, HttpServletResponse response, @RequestBody DOCXBaseWrapper docxBaseWrapper) {
//        URL resource = this.getClass().getClassLoader().getResource("templates/NNC1_BRN.docx");
        String templateName = "";
        if (Objects.equals(docxBaseWrapper.getLangType(), 0)) {
            templateName = docxBaseWrapper.EN_FILE_NAME();
        } else if (Objects.equals(docxBaseWrapper.getLangType(), 1)) {
            templateName = docxBaseWrapper.CN_FILE_NAME();
        }

        if (StringUtils.isEmpty(templateName)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        ClassPathResource classPathResource = new ClassPathResource("templates/" + templateName);
        InputStream is = null;
        try {
            is = classPathResource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File outFile = null;
        String extension = FilenameUtils.getExtension(templateName);
        String baseName = FilenameUtils.getBaseName(templateName);
        String dateDir = DateUtil.format(new Date(), "yyyy-MM-dd");
        String timeSuffix = DateUtil.format(new Date(), "yyyy-MM-dd_HH_mm_ss");
        String outFileName = UUID.randomUUID().toString().substring(0, 6) + "-" + baseName + "-" + timeSuffix + "." + extension;

        String dir = mediaConfig.getRootPath() + File.separator + dateDir;

        try {
            switch (extension) {
                case "doc":
//                    handlerByDocFile(is);
                    // 暂不处理doc
                    break;
                case "docx":
                    outFile = handlerByDocxFile(is, docxBaseWrapper, outFileName, dir);
                    break;
                default:
                    throw new IllegalArgumentException("不能解析的文档类型，请输入正确的word文档类型的文件！");
            }
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        return docxService.save(outFile);
    }

    public static File handlerByDocxFile(InputStream is, DOCXBaseWrapper docxBaseWrapper, String outFileName, String timeSuffix) throws IOException, InvalidFormatException {
        XWPFDocument xwpfDocument = new XWPFDocument(is);
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();

        List<String> paragraphData = docxBaseWrapper.getParagraphData();
        List<String> tableData = docxBaseWrapper.getTableData();

        ReplaceTypeEnum replaceType = docxBaseWrapper.replaceType();

        int pPos = 0;
        int tPos = 0;

        // 处理段落
        for (XWPFParagraph paragraph : paragraphs) {
            pPos = replaceData(paragraphData, pPos, paragraph, true, replaceType);
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
                        tPos = replaceData(tableData, tPos, xwpfParagraph, false, replaceType);
                    }
                }
            }
        }

        //生成新的word
        File file = new File(timeSuffix, outFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);
        xwpfDocument.write(stream);
        stream.close();

        xwpfDocument.close();
        is.close();

        return file;
    }

    private static int replaceData(List<String> dataList, int pos, XWPFParagraph xwpfParagraph, boolean isParagraph, ReplaceTypeEnum replaceType) {
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        for (XWPFRun run : runs) {
            if (matched(run, replaceType)) {
                System.out.println((isParagraph ? "段落参数：" : "表格参数：") + run);
                String data = "";
                if (!CollectionUtils.isEmpty(dataList) && pos < dataList.size()) {
                    data = dataList.get(pos++);
                }
                run.setText(replaceText(run.toString(), data, replaceType), 0);
                if (Objects.equals(replaceType, ReplaceTypeEnum.RED_TEXT)) {
                    // 这里要再改为黑色
                    run.setColor(replaceType.getReplaceColor());
                }
            }
        }
        return pos;
    }

    private static boolean matched(XWPFRun run, ReplaceTypeEnum replaceType) {
        if (Objects.equals(replaceType, ReplaceTypeEnum.RED_TEXT)) {
            return Objects.equals(run.getColor(), replaceType.getColor());
        }
        return run.toString().matches("(\\{\\{.*}})");
    }

    public static String replaceText(String text, String replaceText, ReplaceTypeEnum replaceType) {
        if (Objects.equals(replaceType, ReplaceTypeEnum.RED_TEXT)) {
            return replaceText;
        }
        if (text.matches("(\\{\\{.*}})")) {
            return text.replaceAll("(\\{\\{.*}})", replaceText);
        }
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }
}
