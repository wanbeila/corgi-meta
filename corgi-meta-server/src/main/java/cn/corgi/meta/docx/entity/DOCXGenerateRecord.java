package cn.corgi.meta.docx.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
@Data
public class DOCXGenerateRecord {

    private Long id;
    private String filename;
    private String filePath;
    private Date createTime;
}
