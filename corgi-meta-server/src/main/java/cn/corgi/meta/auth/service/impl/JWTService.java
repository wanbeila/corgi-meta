package cn.corgi.meta.auth.service.impl;

import cn.corgi.meta.auth.config.AuthingConfig;
import cn.corgi.meta.base.util.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JWTService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    private static final String TOKEN_PREFIX = "corgi:token:";

    private final AuthingConfig authingConfig;

    public String extractUsername(String token) {
        // 这里修改为检查redis中token是否过期
        String username = extractClaim(token, Claims::getSubject);
        Boolean hasKey = RedisUtils.getStringRedisTemplate().hasKey(TOKEN_PREFIX + username);
        if (Boolean.FALSE.equals(hasKey)) {
            throw new ExpiredJwtException(null, null, "token已过期！");
        }
        // 自动续期
        RedisUtils.getStringRedisTemplate().expire(TOKEN_PREFIX + username, authingConfig.getTokenExpire(), TimeUnit.SECONDS);
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return false;
//        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String generateTokenWithLock(String username) {
        Map<String, Object> claims = new HashMap<>();
        AtomicReference<String> atomicToken = new AtomicReference<>();
        // 如果redis已存在token，则直接返回
        RedisUtils.doWithLock(username, () -> {
            String token = RedisUtils.getStringRedisTemplate().opsForValue().get(TOKEN_PREFIX + username);
            if (StringUtils.isNotEmpty(token)) {
                atomicToken.set(token);
            } else {
                log.info("生成新的token");
                String newToken = createToken(claims, username);
                atomicToken.set(newToken);
                log.info("redis缓存token={}", newToken);
                RedisUtils.getStringRedisTemplate().opsForValue().set(TOKEN_PREFIX + username, newToken, authingConfig.getTokenExpire(), TimeUnit.SECONDS);
            }
        });
        return atomicToken.get();
    }


    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 12小时过期
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
