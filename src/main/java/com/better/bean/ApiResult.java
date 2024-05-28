package com.better.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: JeanRiver
 * @Description:
 * @Date Created at 15:45 2022/5/24
 * @Modified By:
 */
@Getter
@Setter
@Schema(description = "通用响应数据")
public class ApiResult<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    @Schema(description = "业务状态码,用户未登录为10001")
    private Integer code;

    @Schema(description = "")
    private Integer errno;

    @Schema(description = "消息")
    private String message;

    @Schema(description = "数据主体")
    private T data;

    public ApiResult() {
    }

    public ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.errno = code;
        this.message = message;
        this.data = data;
    }

    public ApiResult(T data) {
        this(0, DEFAULT_SUCCESS_MESSAGE, data);
    }


    public static <T> ApiResult<T> ok() {
        return new ApiResult<>(0, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static <T> ApiResult<T> ok(String message) {
        return new ApiResult<>(0, message, null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(0, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static ApiResult<String> error(Integer errorCode, String errMsg) {
        return new ApiResult<>(errorCode, errMsg, null);
    }
}
