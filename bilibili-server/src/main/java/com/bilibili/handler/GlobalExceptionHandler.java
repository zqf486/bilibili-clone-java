package com.bilibili.handler;

import com.bilibili.constant.MessageConstant;
import com.bilibili.enumeration.ValidationMessageEnum;
import com.bilibili.enumeration.ResponseEnum;
import com.bilibili.exception.AuthException;
import com.bilibili.exception.BusinessException;
import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获空路由请求
     *
     * @return 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result handle404Exception() {
        return new Result(
                ResponseEnum.NOT_FOUND.getStatus(),
                ResponseEnum.NOT_FOUND.getCode(),
                ResponseEnum.NOT_FOUND.getInfo(),
                null
        );
    }

    /**
     * 捕获业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBizException(BusinessException e) {
        log.info("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 捕获参数类型不匹配异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("请求参数异常: {}, 位于: {}", e.getName(), e.getParameter());
        return Result.error(MessageConstant.TYPE_MISMATCH + " : " + e.getParameter());
    }

    /**
     * 捕获 未登录 / 未认证
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AuthException.class)
    public Result handleAuthException(AuthException e) {
        log.info("鉴权拦截: {}", e.getMessage());
        return new Result(
                ResponseEnum.UNAUTHORIZED.getStatus(),
                ResponseEnum.UNAUTHORIZED.getCode(),
                ResponseEnum.UNAUTHORIZED.getInfo(),
                null
        );
    }

    /**
     * 捕获 Bean Validation 校验异常
     *
     * @param ex
     * @return Map<field, message>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException ex) {
        List<HashMap> errors = ex.getBindingResult().getFieldErrors().stream().map(err -> {
            HashMap hashMap = new HashMap();
            hashMap.put("field", err.getField());

            String msg = err.getDefaultMessage();
            if (ValidationMessageEnum.REGISTER_PASSWORD.getErrorField().equals(err.getField())) {
                msg = ValidationMessageEnum.REGISTER_PASSWORD.getErrorMsg();
            }
            hashMap.put("message", msg);
            return hashMap;
        }).collect(Collectors.toList());
        log.info("参数校验失败: {}", errors);
        return Result.error("参数校验失败", errors);
    }

    /**
     * 捕获 sql 唯一约束异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.info("sql 唯一约束异常: {}", e.getMessage());
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String value = split[2];
            String msg = value + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            return new Result(
                    ResponseEnum.SERVER_ERROR.getStatus(),
                    ResponseEnum.SERVER_ERROR.getCode(),
                    ResponseEnum.SERVER_ERROR.getInfo(),
                    null
            );
        }
    }

    /**
     * 捕获请求方式异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new Result(
                ResponseEnum.METHOD_NOT_ALLOWED.getStatus(),
                ResponseEnum.METHOD_NOT_ALLOWED.getCode(),
                ResponseEnum.METHOD_NOT_ALLOWED.getInfo(),
                null
        );
    }

    /**
     * 捕获请求体错误 / 缺失
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return Result.error(MessageConstant.HTTP_MESSAGE_NOT_READABLE);
    }

    /**
     * 捕获未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("未知异常", e);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
