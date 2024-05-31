package cn.corgi.meta.base.bean;

import cn.corgi.meta.base.constants.ResultCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanbeila
 * @date 2024/5/31
 */
@Data
public class ResultInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -192745776688863849L;

    private int code;
    private String message;
    private Map<String, Object> result;

    public ResultInfo() {
        this(ResultCodeEnum.OK.getValue(), ResultCodeEnum.OK.getLabel());
    }

    public ResultInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultInfo addResultData(String key, Object value) {
        if (null == this.result) {
            this.result = new HashMap<>();
        }
        result.put(key, value);
        return this;
    }

    public static ResultInfo newInstance() {
        return new ResultInfo();
    }

    public static ResultInfo newInstance(int code, String message) {
        return new ResultInfo(code, message);
    }
}
