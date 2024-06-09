package cn.corgi.meta.base.lock;

/**
 * @author wanbeila
 * @date 2024/6/9
 */
@FunctionalInterface
public interface InLockFunction {

    void doWithLock();
}
