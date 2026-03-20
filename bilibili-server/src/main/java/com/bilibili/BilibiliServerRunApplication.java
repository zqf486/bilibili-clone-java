package com.bilibili;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bilibili.mapper")
@Slf4j
public class BilibiliServerRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(BilibiliServerRunApplication.class, args);
        log.info("server started up");
    }
}