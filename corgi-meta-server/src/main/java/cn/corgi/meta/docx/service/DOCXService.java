package cn.corgi.meta.docx.service;

import cn.corgi.meta.docx.entity.DOCXGenerateRecord;

import java.io.File;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
public interface DOCXService {
    DOCXGenerateRecord save(File outFile);
}
