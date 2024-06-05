package cn.corgi.meta.base.util;

import cn.corgi.meta.base.context.ApplicationContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
public class RedisUtils {

    private RedisUtils() {}

    private static final StringRedisTemplate stringRedisTemplate;

    static {
        stringRedisTemplate = ApplicationContextHolder.getApplicationContext().getBean(StringRedisTemplate.class);
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }
}
