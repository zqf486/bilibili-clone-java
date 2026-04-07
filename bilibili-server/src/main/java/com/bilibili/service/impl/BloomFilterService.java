package com.bilibili.service.impl;

import com.bilibili.service.IBloomFilterService;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BloomFilterService implements IBloomFilterService {
    @Resource
    private RedissonClient redissonClient;

    private final Map<String, RBloomFilter<?>> cache = new ConcurrentHashMap<>();

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
    ) {
        return (RBloomFilter<T>) cache.computeIfAbsent(filterName, key -> {
            RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(key);
            bloomFilter.tryInit(expectedInsertions, falseProbability);
            return bloomFilter;
        });
    }
}
