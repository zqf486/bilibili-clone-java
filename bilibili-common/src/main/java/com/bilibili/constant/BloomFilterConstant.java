package com.bilibili.constant;

public class BloomFilterConstant {
    public static final String DEFAULT_SUFFIX = "bloom";
    /**
     * 默认预计插入数量
     */
    public static final Long DEFAULT_EXPECTED_INSERTIONS = 1000_000L;
    /**
     * 默认误判率
     */
    public static final Double DEFAULT_FALSE_PROBABILITY = 0.01;
}
