package com.bilibili.constant;

public class RedisConstant {
    /**
     * 空值的ttl
     */
    public static final long CACHE_NULL_TTL = 60;
    /**
     * 永久缓存
     */
    public static final Long FOREVER_TTL = -1L;
    /**
     * redis key 前缀
     */
    public static final String REDIS_KEY_PREFIX = "bilibili:";
    /**
     * redis cache 前缀
     */
    public static final String REDIS_CACHE_PREFIX = REDIS_KEY_PREFIX + "cache:";
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
     * 随机过期时间范围 单位（s）
     */
    public static final int REDIS_KEY_RADOM_EXPIRES_RANGE = 60 * 5;
    /**
     * web端token
     */
    public static final String REDIS_TOKEN_KEY_WEB = REDIS_KEY_PREFIX + "token:web:";
    /**
     * UserInfoVO redis key 缓存前缀
     */
    public static final String CACHE_USER_KEY = REDIS_CACHE_PREFIX + "user:";
    /**
     * Category redis key
     */
    public static final String CACHE_CATEGORY_KEY = REDIS_CACHE_PREFIX + "category";
}
