package cn.corgi.meta.docx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author wanbeila
 * @date 2024/6/8
 */
@Getter
@RequiredArgsConstructor
public enum ReplaceTypeEnum {

    PARAMETER(0, "变量替换", null, null),
    RED_TEXT(1, "红色文字识别替换", "FF0000", "000000"),

    ;

    private final int value;
    private final String label;
    private final String color;
    private final String replaceColor;
}
