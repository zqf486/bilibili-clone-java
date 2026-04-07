package com.bilibili.init;

import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BloomFilterInitRunner implements CommandLineRunner {

    @Resource
    private List<BloomFilterPreloader> preloaders;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        if (preloaders == null || preloaders.isEmpty()) return;

        for(BloomFilterPreloader preloader : preloaders){
            preloader.preload();
        }
    }
}
