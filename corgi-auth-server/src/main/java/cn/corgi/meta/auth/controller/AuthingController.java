package cn.corgi.meta.auth.controller;

import cn.corgi.meta.auth.bean.AuthingVO;
import cn.corgi.meta.auth.bean.AuthingWrapper;
import cn.corgi.meta.auth.entity.User;
import cn.corgi.meta.auth.service.JWTService;
import cn.corgi.meta.auth.service.impl.DefaultUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanbeila
 * @date 2024/4/30
 */

@Tag(name = "认证相关", description = "认证相关的接口")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("authing")
@RestController
public class AuthingController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("login")
    @Operation(summary = "登录")
    public AuthingVO login(@RequestBody AuthingWrapper authingWrapper) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(authingWrapper.getUsername(), authingWrapper.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationRequest);
        log.info("authenticate {}", authenticate);
        if (authenticate.isAuthenticated()) {
            String token = jwtService.generateToken(authingWrapper.getUsername());
            Object userObj = authenticate.getPrincipal();
            AuthingVO authingVO = new AuthingVO();
            authingVO.setToken(token);
            if (userObj instanceof User user) {
                authingVO.setUsername(user.getUsername());
                authingVO.setUserId(user.getId());
            }
            return authingVO;
        }
        throw new UsernameNotFoundException("invalid user request！");
    }

    @PostMapping("get-user-info")
    @Operation(summary = "获取用户信息")
    public AuthingVO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Object userObj = authentication.getPrincipal();
            AuthingVO authingVO = new AuthingVO();
            if (userObj instanceof User user) {
                authingVO.setUsername(user.getUsername());
                authingVO.setUserId(user.getId());
            }
            return authingVO;
        }
        throw new AccessDeniedException("invalid token！");
    }
}
