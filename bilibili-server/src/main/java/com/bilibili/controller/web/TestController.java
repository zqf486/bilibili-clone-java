package com.bilibili.controller.web;

import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public Result test() {
        long randomTTL = new Random().nextInt(100);
        log.info("Test: {}", randomTTL);
        return Result.success();
    }
}
