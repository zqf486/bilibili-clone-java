package com.bilibili.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 响应参数
 */
@Getter
public enum ResponseEnum {
    SUCCESS("请求成功", "success", HttpStatus.OK.value()),
    FAILED("请求失败", "failed", HttpStatus.BAD_REQUEST.value()),
    UNAUTHORIZED("未登录或认证失败", "error", HttpStatus.UNAUTHORIZED.value()),
    FORBIDDEN("没有权限", "error", HttpStatus.FORBIDDEN.value()),
    NOT_FOUND("资源不存在", "error", HttpStatus.NOT_FOUND.value()),
    SERVER_ERROR("服务器内部错误", "error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    METHOD_NOT_ALLOWED("不支持的请求方式", "error", HttpStatus.METHOD_NOT_ALLOWED.value());

    private final String info;
    private final String status;
    private final Integer code;

    ResponseEnum(String info, String status, Integer code) {
        this.info = info;
        this.status = status;
        this.code = code;
    }
}
