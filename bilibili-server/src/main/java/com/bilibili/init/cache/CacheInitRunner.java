package com.bilibili.init.cache;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CacheInitRunner implements CommandLineRunner {

    @Resource
    private List<CachePreLoader> preLoaders;

    @Override
    public void run(String... args) throws Exception {

        if (preLoaders == null || preLoaders.isEmpty()) {
            log.info("没有找到缓存预热器");
            return;
        }

        log.info("开始进行缓存预热");

        for (CachePreLoader preLoader : preLoaders) {
            try {
                preLoader.preload();
            } catch (Exception e) {
                log.error("缓存预热失败", e);
            }
        }

        log.info("缓存预热完成, 共 {} 个预热器", preLoaders.size());
    }
}
