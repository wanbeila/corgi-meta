package cn.corgi.meta.auth.bean;

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
    private List<Map<String, Object>> roles;

    public AuthingVO() {
        roles = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("roleName", "Common User");
        data.put("value", "common");
        roles.add(data);
    }
}
