package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.constant.RedisConstant;
import com.bilibili.entity.TbUser;
import com.bilibili.init.BloomFilterPreloader;
import com.bilibili.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserBloomFilterPreloader implements BloomFilterPreloader {

    @Resource
    private BloomFilterService bloomFilterService;

    @Resource
    private UserMapper userMapper;

    /**
     * 执行预热逻辑
     */
    @Override
    public void preload() {

        log.info("开始预热 tb_user 布隆过滤器...");

        String key = RedisConstant.CACHE_USER_KEY + "bloom";

        RBloomFilter<Long> userBloom = bloomFilterService.get(key, 1000000L, 0.01);

        long cursor = 0;
        int batchSize = 1000;

        while (true){
            List<TbUser> tbUsers = userMapper.selectList(
                    new LambdaQueryWrapper<TbUser>()
                            .select(TbUser::getId)
                            .gt(TbUser::getId, cursor)
                            .orderByAsc(TbUser::getId)
                            .last("limit " + batchSize)
            );

            if (tbUsers.isEmpty()) break;

            tbUsers.forEach(user -> userBloom.add(user.getId()));

            cursor = tbUsers.get(tbUsers.size() - 1).getId();
        }
    }
}
