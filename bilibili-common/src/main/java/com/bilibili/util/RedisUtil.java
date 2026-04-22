package com.bilibili.util;

import com.bilibili.constant.CacheConstant;
import com.bilibili.result.CacheResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

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
    public <T> void set(String key, T value, Long timeout) {
        try {

            String json = objectMapper.writeValueAsString(value);

            if (timeout < 0) {
                stringRedisTemplate.opsForValue().set(key, json);
                return;
            }

            stringRedisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);

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
    public <T> T get(String key, Class<T> clazz) {
        try {

            String json = stringRedisTemplate.opsForValue().get(key);

            if (json == null) {
                return null;
            }

            if (CacheConstant.NULL.equals(json)) {
                return null;
            }

            return objectMapper.readValue(json, clazz);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 缓存空值
     *
     * @param key
     * @param timeout
     */
    public void cacheNull(String key, Long timeout) {

        if (timeout < 0) {
            return;
        }

        stringRedisTemplate.opsForValue().set(key, CacheConstant.NULL, timeout, TimeUnit.SECONDS);
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
    public <R> CacheResult<R> getCache(String key, Class<R> clazz) {
        try {
            String json = stringRedisTemplate.opsForValue().get(key);

            if (json == null) {
                return new CacheResult<>(false, false, null);
            }

            if (CacheConstant.NULL.equals(json)) {
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
