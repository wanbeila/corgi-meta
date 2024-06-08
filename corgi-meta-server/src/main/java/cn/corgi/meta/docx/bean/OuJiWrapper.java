package cn.corgi.meta.docx.bean;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@JsonTypeName(value = "ouji")
@Data
public class OuJiWrapper extends DOCXBaseWrapper {

    private static final String FILE_NAME = "歐記A516-年審";
    public static final String EN_FILE_NAME = "歐記A516-年審英文.docx";
    public static final String CN_FILE_NAME = "歐記A516-年審中文.docx";

    @Override
    public String EN_FILE_NAME() {
        return EN_FILE_NAME;
    }

    @Override
    public String CN_FILE_NAME() {
        return CN_FILE_NAME;
    }
}
