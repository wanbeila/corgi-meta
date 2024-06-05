package cn.corgi.meta.docx.bean;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
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
}
