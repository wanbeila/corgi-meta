package cn.corgi.meta.docx.bean;

import lombok.Data;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Data
public class HaiMingWrapper extends DOCXBaseWrapper {

    private static final String FILE_NAME = "haiming-32-";
    public static final String EN_FILE_NAME = "haiming-32-en.docx";
    public static final String CN_FILE_NAME = "haiming-32-cn.docx";

    @Override
    public String EN_FILE_NAME() {
        return EN_FILE_NAME;
    }

    @Override
    public String CN_FILE_NAME() {
        return CN_FILE_NAME;
    }
}
