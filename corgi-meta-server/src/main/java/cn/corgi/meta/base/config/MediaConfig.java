package cn.corgi.meta.base.config;

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
@EnableConfigurationProperties(MediaConfig.class)
@ConfigurationProperties(prefix = "corgi.media")
public class MediaConfig {

    private String rootPath;
}
