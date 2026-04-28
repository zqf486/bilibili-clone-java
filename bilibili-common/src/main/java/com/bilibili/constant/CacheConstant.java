package com.bilibili.constant;

public class CacheConstant {
    /**
     * cache 前缀
     */
    public static final String CACHE_KEY_PREFIX = RedisConstant.REDIS_KEY_PREFIX + "cache:";
    /**
     * 空值
     */
    public static final String NULL = "NULL";
    /**
     * 空值的ttl
     */
    public static final long CACHE_NULL_TTL = 60;

    /**
     * 业务key名称
     */
    public static final String USER_KEY = CACHE_KEY_PREFIX + "user:";
    public static final String CATEGORY_TREE_KEY = CACHE_KEY_PREFIX + "category:tree:all";
    public static final String CATEGORY_ENABLED_TREE_KEY = CACHE_KEY_PREFIX + "category:tree:enabled";
}
