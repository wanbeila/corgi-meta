package cn.corgi.meta.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
@Configuration
@Data
@EnableConfigurationProperties(AuthingConfig.class)
@ConfigurationProperties(prefix = "corgi.authing")
public class AuthingConfig {

    // 过期时间，单位秒，默认 24小时过期
    private Integer tokenExpire = 24 * 60 * 60;
}
