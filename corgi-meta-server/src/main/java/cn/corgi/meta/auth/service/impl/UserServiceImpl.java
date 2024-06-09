package cn.corgi.meta.auth.service.impl;

import cn.corgi.meta.auth.bean.AuthingVO;
import cn.corgi.meta.auth.domain.RoleMapper;
import cn.corgi.meta.auth.domain.UserMapper;
import cn.corgi.meta.auth.entity.Role;
import cn.corgi.meta.auth.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanbeila
 * @date 2024/6/9
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @Override
    public void fillUserInfo(AuthingVO authingVO) {
        // 补充角色信息
        List<Role> roles = roleMapper.getRolesByUserId(Long.valueOf(authingVO.getUserId()));
        authingVO.setRoles(roles);
    }
}
