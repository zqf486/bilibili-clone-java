package com.bilibili.init.cache.impl;

import com.bilibili.init.cache.CachePreLoader;
import com.bilibili.service.ICategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryCachePreloader implements CachePreLoader {

    @Resource
    private ICategoryService categoryService;

    @Override
    public void preload() {
        log.info("开始重建分类缓存");
        categoryService.refreshCache();
        log.info("分类缓存重建完成");
    }
}
