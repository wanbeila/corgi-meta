package cn.corgi.meta.auth.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
public class DefaultPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(rawPassword.toString(), encodedPassword);
    }
}
