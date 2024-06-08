package cn.corgi.meta.docx.bean;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@JsonTypeName(value = "haiming-filing")
@Data
public class HaiMingFilingWrapper extends DOCXBaseWrapper {

    private static final String FILE_NAME = "海銘備案";
    public static final String EN_FILE_NAME = "海銘備案.docx";
    public static final String CN_FILE_NAME = "海銘備案.docx";

    @Override
    public String EN_FILE_NAME() {
        return EN_FILE_NAME;
    }

    @Override
    public String CN_FILE_NAME() {
        return CN_FILE_NAME;
    }
}
