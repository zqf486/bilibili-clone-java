package com.bilibili.constant;

public class BloomFilterConstant {
    /**
     * 布隆过滤器rediskey prefix
     */
    public static final String DEFAULT_PREFIX = RedisConstant.REDIS_KEY_PREFIX + "bloom:";
    /**
     * 默认预计插入数量
     */
    public static final Long DEFAULT_EXPECTED_INSERTIONS = 1000_000L;
    /**
     * 默认误判率
     */
    public static final Double DEFAULT_FALSE_PROBABILITY = 0.01;

    /**
     * 业务key
     */
    public static final String USER_KEY = DEFAULT_PREFIX + "user";
}
