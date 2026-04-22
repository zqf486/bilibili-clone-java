package com.bilibili.constant;

public class CheckCodeConstant {

    public static final int WIDTH = 200;
    public static final int HEIGHT = 50;

    /**
     * 验证码 key
     */
    public static final String REDIS_KEY_CHECK_CODE = RedisConstant.REDIS_KEY_PREFIX + "checkcode:";
    /**
     * 过期时间
     */
    public static final Long EXPIRES = RedisConstant.REDIS_KEY_EXPIRES_ONE_MIN * 2;
}
