package cn.corgi.meta.auth.domain;

import cn.corgi.meta.auth.entity.Role;
import cn.corgi.meta.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Mapper
public interface RoleMapper {

    @Select("select id, name as roleName, value from role r left join user_role hr on r.id = hr.role_id where user_id = #{userId}")
    List<Role> getRolesByUserId(@Param("userId") Long userId);
}
