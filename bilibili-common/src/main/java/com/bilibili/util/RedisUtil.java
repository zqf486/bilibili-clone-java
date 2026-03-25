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
    public void set(String key, Object value, long time) {
        if (time < 0) {
            redisTemplate.opsForValue().set(key, value);
            return;
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

    /**
     * 重置 key 过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        if (key.isBlank()) {
            return false;
        }

        if (time <= 0) {
            return false;
        }

        return Boolean.TRUE.equals(
                redisTemplate.expire(key, time, TimeUnit.SECONDS)
        );
    }

    /**
     * 获取 key 过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        if (key == null || key.isEmpty()){
            return null;
        }
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
