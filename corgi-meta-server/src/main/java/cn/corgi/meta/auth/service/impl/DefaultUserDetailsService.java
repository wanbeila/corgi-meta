package cn.corgi.meta.auth.service.impl;

import cn.corgi.meta.auth.domain.UserMapper;
import cn.corgi.meta.auth.entity.User;
import cn.corgi.meta.base.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUsername = userMapper.getUserByUsername(username);
        if (userByUsername == null) {
            throw new GlobalException("用户不存在！");
        }
        return userByUsername;
    }
}
