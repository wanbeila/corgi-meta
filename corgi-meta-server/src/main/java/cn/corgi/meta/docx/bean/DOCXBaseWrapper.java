package cn.corgi.meta.docx.bean;

import cn.corgi.meta.docx.constant.ReplaceTypeEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "fileName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HaiMingFilingWrapper.class, name = "haiming-filing"),
        @JsonSubTypes.Type(value = OuJiFilingWrapper.class, name = "ouji-filing")})
@Data
public class DOCXBaseWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = -8921341252434741050L;

    /**
     * 用于序列化
     */
    private String fileName = "base";
    /**
     * 0 英文 1 中文
     */
    private Integer langType;
    private List<String> paragraphData;
    private List<String> tableData;

    public String EN_FILE_NAME() {
        return "";
    }
    public String CN_FILE_NAME() {
        return "";
    }

    /**
     * 默认为变量替换方式
     */
    public ReplaceTypeEnum replaceType() {
        return ReplaceTypeEnum.PARAMETER;
    }
}
