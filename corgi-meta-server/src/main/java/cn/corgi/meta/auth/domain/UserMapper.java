package cn.corgi.meta.auth.domain;

import cn.corgi.meta.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User getUserByUsername(@Param("username") String username);
}
