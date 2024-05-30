package cn.corgi.meta.docx.bean;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Data
public class HaiMingWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = -8921341252434741050L;

    /**
     * 0 英文 1 中文
     */
    private Integer langType;
    private Map<String, Object> keyAndValue;

    private static final String FILE_NAME = "haiming-32-";
    public static final String EN_FILE_NAME = "haiming-32-en.docx";
    public static final String CN_FILE_NAME = "haiming-32-cn.docx";



}
