package cn.corgi.meta.docx.service.impl;

import cn.corgi.meta.docx.domain.DOCXGenerateRecordMapper;
import cn.corgi.meta.docx.entity.DOCXGenerateRecord;
import cn.corgi.meta.docx.service.DOCXService;
import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
@RequiredArgsConstructor
@Service
public class DOCXServiceImpl implements DOCXService {

    private final DOCXGenerateRecordMapper docxGenerateRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DOCXGenerateRecord save(File outFile) {
        DOCXGenerateRecord record = new DOCXGenerateRecord();
        record.setFilename(outFile.getName());
        String filePath = FileUtil.normalize(outFile.getPath());
        record.setFilePath(filePath);
        record.setCreateTime(new Date());
        docxGenerateRecordMapper.insert(record);
        return record;
    }
}
