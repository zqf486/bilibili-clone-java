package com.bilibili.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bilibili.enumeration.ResponseEnum.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String info;
    private String status;
    private T data;

    public Result(String status, Integer code, String info, T data){
        this.code = code;
        this.info = info;
        this.status = status;
        this.data = data;
    }

    public static <T> Result<T> success(){
        return new Result<>(SUCCESS.getStatus(), SUCCESS.getCode(), SUCCESS.getInfo(), null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(SUCCESS.getStatus(), SUCCESS.getCode(), SUCCESS.getInfo(), data);
    }

    public static <T> Result<T> error(String info){
        return new Result<>(FAILED.getStatus(), FAILED.getCode(), info, null);
    }

    public static <T> Result<T> error(String info, T data) {
        return new Result<>(FAILED.getStatus(), FAILED.getCode(), info, data);
    }

}
