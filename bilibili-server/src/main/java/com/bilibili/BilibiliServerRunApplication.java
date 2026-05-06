package com.bilibili;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableCaching
@EnableScheduling
@MapperScan("com.bilibili.mapper")
@SpringBootApplication
public class BilibiliServerRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(BilibiliServerRunApplication.class, args);
        log.info("server started up");
    }
}