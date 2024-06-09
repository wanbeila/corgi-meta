package cn.corgi.meta.base.util;

import cn.corgi.meta.base.context.ApplicationContextHolder;
import cn.corgi.meta.base.lock.InLockFunction;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
public class RedisUtils {

    private RedisUtils() {}

    @Getter
    private static final StringRedisTemplate stringRedisTemplate;
    @Getter
    private static final Redisson REDISSON;

    private static final String LOCK_KEY = "REDISSON";

    static {
        stringRedisTemplate = ApplicationContextHolder.getApplicationContext().getBean(StringRedisTemplate.class);
        REDISSON = ApplicationContextHolder.getApplicationContext().getBean(Redisson.class);
    }

    public static void doWithLock(String lockKey, InLockFunction function) {
        RLock lock = REDISSON.getLock(lockKey);
        try {
            lock.lock();

            function.doWithLock();

        } finally {
            lock.unlock();
        }
    }

    public static String getValueWithLock(String key) {
        RLock lock = REDISSON.getLock(LOCK_KEY);
        try {
            lock.lock();

            return stringRedisTemplate.opsForValue().get(key);

        } finally {
            lock.unlock();
        }
    }

    public static void setValueWithLock(String key, String value) {
        setValueWithLock(key, value, 60);
    }

    public static void setValueWithLock(String key, String value, long timeout) {
        setValueWithLock(key, value, timeout, TimeUnit.SECONDS);
    }

    public static void setValueWithLock(String key, String value, long timeout, TimeUnit timeUnit) {
        RLock lock = REDISSON.getLock(LOCK_KEY);
        try {
            lock.lock();

            stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);

        } finally {
            lock.unlock();
        }
    }

}
