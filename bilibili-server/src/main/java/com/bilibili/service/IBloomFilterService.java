package com.bilibili.service;

import org.redisson.api.RBloomFilter;

public interface IBloomFilterService {


    /**
     * 创建布隆过滤器
     *
     * @param filterName         过滤器名称
     * @param expectedInsertions 预测插入数量
     * @param falseProbability   误判率
     * @param <T>
     * @return
     */
    public <T> RBloomFilter<T> get(
            String filterName,
            long expectedInsertions,
            double falseProbability
    );
}
