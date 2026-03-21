package com.bilibili.constant;

public class RedisConstant {
    /**
     * redis key 前缀
     */
    private static final String REDIS_KEY_PREFIX = "bilibili:";
    /**
     * 验证码 key
     */
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    /**
     * 过期时间： 60s
     */
    public static final long REDIS_KEY_EXPIRES_ONE_MIN = 60;
}
