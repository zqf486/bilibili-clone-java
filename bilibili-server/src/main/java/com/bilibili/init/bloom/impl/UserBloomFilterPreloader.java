package com.bilibili.init.bloom.impl;

import com.bilibili.constant.BloomFilterConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.init.bloom.BloomFilterPreLoader;
import com.bilibili.mapper.UserMapper;
import com.bilibili.service.IBloomFilterService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserBloomFilterPreloader implements BloomFilterPreLoader {

    @Resource
    private IBloomFilterService bloomFilterService;

    @Resource
    private UserMapper userMapper;

    /**
     * 执行预热
     */
    @Override
    public void preload() {
        List<Long> objects = new ArrayList<>(1);
        objects.add(12345L);
        bloomFilterService.addAll(BloomFilterConstant.USER_KEY, objects);
    }
}
