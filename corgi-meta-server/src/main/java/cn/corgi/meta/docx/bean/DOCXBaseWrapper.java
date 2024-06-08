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
        // 备案
        @JsonSubTypes.Type(value = HaiMingFilingWrapper.class, name = "haiming-filing"),
        @JsonSubTypes.Type(value = OuJiFilingWrapper.class, name = "ouji-filing"),
        // 注册
        @JsonSubTypes.Type(value = HaiMingRegisterWrapper.class, name = "haiming-register"),
        @JsonSubTypes.Type(value = OuJiRegisterWrapper.class, name = "ouji-register"),
        // 年审
        @JsonSubTypes.Type(value = HaiMingAnnualAuditWrapper.class, name = "haiming-annualAudit"),
        @JsonSubTypes.Type(value = OuJiAnnualAuditWrapper.class, name = "ouji-annualAudit"),
        // 改股改董
        @JsonSubTypes.Type(value = ChangeStakeHolderWrapper.class, name = "changeStakeHolder"),
        // 注销
        @JsonSubTypes.Type(value = UnRegisterWrapper.class, name = "unregister"),
})
@Data
public class DOCXBaseWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = -8921341252434741050L;

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
