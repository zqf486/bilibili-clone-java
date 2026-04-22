package com.bilibili.constant;

public class RedisConstant {
    /**
     * redis key 前缀
     */
    public static final String REDIS_KEY_PREFIX = "bilibili:";
    /**
     * 永久缓存
     */
    public static final Long FOREVER_TTL = -1L;
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
}
