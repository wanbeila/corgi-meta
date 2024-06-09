package cn.corgi.meta.auth.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Data
public class Role implements Serializable {

    private Long id;
    @Column(value = "name")
    private String roleName;
    private String value;

}
