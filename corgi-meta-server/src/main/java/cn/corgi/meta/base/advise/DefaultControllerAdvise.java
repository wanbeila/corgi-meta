package cn.corgi.meta.base.advise;

import cn.corgi.meta.base.bean.ResultInfo;
import cn.corgi.meta.base.constant.ResultCodeEnum;
import cn.corgi.meta.base.constant.ResultInfoConst;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author wanbeila
 * @date 2024/5/30
 */
@Slf4j
@RestControllerAdvice
public class DefaultControllerAdvise implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(Exception.class)
    public ResultInfo handleAll(Exception e) {
        log.error("异常：", e);
        return ResultInfo.newInstance(ResultCodeEnum.ERROR.getValue(), e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("supports: {}", returnType.getDeclaringClass().getName());
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null == body) {
            return ResultInfo.newInstance();
        }

        String requestUri = request.getURI().toString();

        if (requestUri.contains("swagger") || requestUri.startsWith("/actuator") || requestUri.endsWith("api-docs")) {
            return body;
        }

        if (body instanceof ResultInfo) {
            return body;
        } else if (body instanceof String) {
            ResultInfo resultInfo = ResultInfo.newInstance();
            return new Gson().toJson(resultInfo.addResultData(ResultInfoConst.RESULT_DATA, body));
        } else {
            ResultInfo resultInfo = ResultInfo.newInstance();
            return resultInfo.addResultData(ResultInfoConst.RESULT_DATA, body);
        }
    }
}
