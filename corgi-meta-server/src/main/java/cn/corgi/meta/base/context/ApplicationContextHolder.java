package cn.corgi.meta.base.context;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    private static void setContext(ApplicationContext context) {
        applicationContext = context;
    }
}
