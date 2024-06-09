package cn.corgi.meta.auth.bean;

import cn.corgi.meta.auth.entity.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanbeila
 * @date 2024/6/3
 */
@Data
public class AuthingVO {

    private String userId;
    private String username;
    private String realName;
    private String token;

    private String homePath = "/dashboard/workbench";
    private List<Role> roles;
}
