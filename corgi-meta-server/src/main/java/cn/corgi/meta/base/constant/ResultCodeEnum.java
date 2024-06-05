package cn.corgi.meta.base.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author wanbeila
 * @date 2024/5/31
 */
@Getter
@RequiredArgsConstructor
public enum ResultCodeEnum {

    OK(0, "ok"),

    ERROR(1, "error"),
    NOT_FOUND(10404, "notFound"),

    ;

    private final int value;
    private final String label;
}
