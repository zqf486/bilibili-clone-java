package com.bilibili.init.bloom;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BloomFilterInitRunner implements CommandLineRunner {

    @Resource
    private List<BloomFilterPreLoader> preLoaders;

    @Override
    public void run(String... args) throws Exception {
        if (preLoaders == null || preLoaders.isEmpty()) {
            log.info("没用找到布隆过滤器预热器");
            return;
        }

        log.info("开始进行布隆过滤器预热");

        for (BloomFilterPreLoader preLoader : preLoaders) {
            try {
                preLoader.preload();
            } catch (Exception e) {
                log.error("布隆过滤器预热失败", e);
            }
        }

        log.info("布隆过滤器预热完成, 共 {} 个预热器", preLoaders.size());
    }
}
