package com.bilibili.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 将值存入 redis 并设置 过期时间
     *
     * @param key
     * @param time
     */
    public void set(String key, String value, long time) {
        if (time < 0) {
            redisTemplate.opsForValue().set(key, value);
        }
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 根据 key 取值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
