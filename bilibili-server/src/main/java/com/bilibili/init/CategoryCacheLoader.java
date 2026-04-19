package com.bilibili.init;

import com.bilibili.service.ICategoryService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryCacheLoader implements CommandLineRunner {

    @Resource
    private ICategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        // 运行缓存重建
        categoryService.rebuildCache();
    }
}
