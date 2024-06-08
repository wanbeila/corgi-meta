package cn.corgi.meta.docx.bean;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@JsonTypeName(value = "changeStakeHolder")
@Data
public class ChangeStakeHolderWrapper extends DOCXBaseWrapper {

    private static final String FILE_NAME = "改股改董";
    public static final String EN_FILE_NAME = "改股改董-英文.docx";
    public static final String CN_FILE_NAME = "改股改董-中文.docx";

    @Override
    public String EN_FILE_NAME() {
        return EN_FILE_NAME;
    }

    @Override
    public String CN_FILE_NAME() {
        return CN_FILE_NAME;
    }
}
