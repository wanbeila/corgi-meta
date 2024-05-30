package cn.corgi.meta.auth.advises;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@RestControllerAdvice
public class DefaultControllerAdvise {

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception e) {
        return e.getMessage();
    }
}
