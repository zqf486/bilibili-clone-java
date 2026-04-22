package com.bilibili.init.bloom.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.constant.BloomFilterConstant;
import com.bilibili.entity.TbUser;
import com.bilibili.init.bloom.BloomFilterPreLoader;
import com.bilibili.mapper.UserMapper;
import com.bilibili.service.IBloomFilterService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

        Long lastId = 0L;
        int batchSize = 1000;
        int count = 0;

        while (true){
            List<Long> ids = userMapper.selectList(
                    new LambdaQueryWrapper<TbUser>()
                            .gt(TbUser::getId, lastId)
                            .orderByAsc(TbUser::getId)
                            .last("limit " + batchSize)
                            .select(TbUser::getId)
            ).stream().map(TbUser::getId).toList();

            if(ids.isEmpty()){
                break;
            }

            count += bloomFilterService.addAll(BloomFilterConstant.USER_KEY, ids);

            lastId = ids.get(ids.size() - 1);
        }

        log.info("过滤器 {} 共插入 {} 条数据", BloomFilterConstant.USER_KEY, count);
    }
}
