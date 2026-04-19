package com.bilibili.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class CheckCodeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片验证码base64
     */
    private String checkCode;

    /**
     * 图片验证码结果 redisKey
     */
    private String checkCodeKey;
}
