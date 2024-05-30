package cn.corgi.meta.auth.controller;

import cn.corgi.meta.auth.bean.AuthingWrapper;
import cn.corgi.meta.auth.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanbeila
 * @date 2024/4/30
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("authing")
@RestController
public class AuthingController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("login")
    public Object login(@RequestBody AuthingWrapper authingWrapper) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(authingWrapper.getUsername(), authingWrapper.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationRequest);
        log.info("authenticate {}", authenticate);
        if (authenticate.isAuthenticated()) {
            String token = jwtService.generateToken(authingWrapper.getUsername());
            return token;
        }
        throw new UsernameNotFoundException("invalid user request！");
    }
}
