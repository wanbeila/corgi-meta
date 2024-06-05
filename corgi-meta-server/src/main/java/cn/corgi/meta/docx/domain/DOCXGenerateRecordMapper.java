package cn.corgi.meta.docx.domain;

import cn.corgi.meta.docx.entity.DOCXGenerateRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
@Mapper
public interface DOCXGenerateRecordMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into docx_generate_record(filename, file_path, create_time) values(#{data.filename}, #{data.filePath}, #{data.createTime})")
    void insert(@Param("data") DOCXGenerateRecord docxGenerateRecord);

    @Select("select * from docx_generate_record")
    List<DOCXGenerateRecord> listAll();
}
