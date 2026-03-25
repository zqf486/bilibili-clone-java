package com.bilibili.constant;

public class RedisConstant {
    /**
     * redis key 前缀
     */
    private static final String REDIS_KEY_PREFIX = "bilibili:";
    /**
     * redis cache 前缀
     */
    private static final String REDIS_CACHE_PREFIX = "cache:";
    /**
     * 验证码 key
     */
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    /**
     * 过期时间： 60s
     */
    public static final long REDIS_KEY_EXPIRES_ONE_MIN = 60;
    /**
     * 过期时间： 1 day
     */
    public static final long REDIS_KEY_EXPIRES_ONE_DAY = 60 * 60 * 24;
    /**
     * web端token
     */
    public static final String REDIS_TOKEN_KEY_WEB = REDIS_KEY_PREFIX + "token:web:";
    /**
     * UserInfoVO redis key 缓存前缀
     */
    public static final String USER_CACHE_KEY = REDIS_KEY_PREFIX + REDIS_CACHE_PREFIX + "user";
}
