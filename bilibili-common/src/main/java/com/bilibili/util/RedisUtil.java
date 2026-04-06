package com.bilibili.util;

import com.bilibili.result.CacheResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 将值存入 redis 并设置 过期时间
     *
     * @param key
     * @param timeout 单位 (s)
     */
    public <T, ID> void set(String keyPrefix, ID key, T value, Long timeout) {
        try {
            String json = null;

            json = objectMapper.writeValueAsString(value);

            if (timeout < 0) {
                stringRedisTemplate.opsForValue().set(keyPrefix + key, json);
                return;
            }

            stringRedisTemplate.opsForValue().set(keyPrefix + key, json, timeout, TimeUnit.SECONDS);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据 key 取值
     *
     * @param key
     * @return
     */
    public <T> T get(String keyPrefix, String key, Class<T> clazz) {
        try {

            String json = stringRedisTemplate.opsForValue().get(keyPrefix + key);

            if (json == null) {
                return null;
            }

            if ("".equals(json)) {
                return null;
            }

            return objectMapper.readValue(json, clazz);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据 key 读取缓存
     *
     * @param key   缓存key
     * @param clazz data类
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public <R, ID> CacheResult<R> getCache(String keyPrefix, ID key, Class<R> clazz) {
        try {
            String json = stringRedisTemplate.opsForValue().get(keyPrefix + key);

            if (json == null) {
                return new CacheResult<>(false, false, null);
            }

            if ("".equals(json)) {
                return new CacheResult<>(true, true, null);
            }

            R data = objectMapper.readValue(json, clazz);

            return new CacheResult<>(true, false, data);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public boolean delete(String keyPrefix, String key) {
        return stringRedisTemplate.delete(keyPrefix + key);
    }

    /**
     * 设置 key 过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String keyPrefix, String key, long time) {

        if (key.isBlank()) {
            return false;
        }

        if (time <= 0) {
            return false;
        }

        return Boolean.TRUE.equals(
                stringRedisTemplate.expire(keyPrefix + key, time, TimeUnit.SECONDS)
        );
    }

    /**
     * 获取 key 过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String keyPrefix, String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        return stringRedisTemplate.getExpire(keyPrefix + key, TimeUnit.SECONDS);
    }
}
