package cn.corgi.meta;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("test")
@SpringBootApplication
public class MetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaApplication.class, args);
    }

    @RequestMapping("test")
    public void test() {
//        URL resource = this.getClass().getClassLoader().getResource("templates/NNC1_BRN.docx");
        URL resource = this.getClass().getClassLoader().getResource("templates/haiming-32-cn.docx");
        File file = new File(resource.getFile());
        String extension = FilenameUtils.getExtension(file.getName());
        try {
            InputStream is = new FileInputStream(file);
            switch (extension) {
                case "doc":
//                    handlerByDocFile(is);
                    // 暂不处理doc
                    break;
                case "docx":
                    handlerByDocxFile(is);
                    break;
                default:
                    throw new IllegalArgumentException("不能解析的文档类型，请输入正确的word文档类型的文件！");
            }
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handlerByDocxFile(InputStream is) throws IOException, InvalidFormatException {
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
