package com.bilibili.init.cache.impl;

import com.bilibili.constant.CacheConstant;
import com.bilibili.init.cache.CachePreLoader;
import com.bilibili.service.ICategoryService;
import com.bilibili.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryCachePreloader implements CachePreLoader {

    @Resource
    private ICategoryService categoryService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void preload() {
        log.info("开始重建分类缓存");
        redisUtil.delete(CacheConstant.CATEGORY_TREE_KEY);
        categoryService.refreshCache();
        log.info("分类缓存重建完成");
    }
}
