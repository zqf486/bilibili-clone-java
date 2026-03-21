package com.bilibili.handler;

import com.bilibili.constant.MessageConstant;
import com.bilibili.enumeration.ValidationMessageEnum;
import com.bilibili.enumeration.ResponseEnum;
import com.bilibili.exception.BusinessException;
import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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
     * 捕获 Bean Validation 校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException ex) {
        List<HashMap> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> {
                    HashMap hashMap = new HashMap();
                    hashMap.put("field", err.getField());

                    String msg = err.getDefaultMessage();
                    if (ValidationMessageEnum.REGISTER_PASSWORD.getErrorField().equals(err.getField())) {
                        msg = ValidationMessageEnum.REGISTER_PASSWORD.getErrorMsg();
                    }
                    hashMap.put("message", msg);
                    return hashMap;
                })
                .collect(Collectors.toList());
        log.info("参数校验失败: {}", errors);
        return Result.error("参数校验失败", errors);
    }

    /**
     * 捕获 sql 唯一约束异常
     *
     * @param ex
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
            return new Result(ResponseEnum.SERVER_ERROR.getStatus(), ResponseEnum.SERVER_ERROR.getCode(), ResponseEnum.SERVER_ERROR.getInfo(), null);
        }
    }

    /**
     * 捕获未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
