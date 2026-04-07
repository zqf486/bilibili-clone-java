package com.bilibili.cache;

import com.bilibili.service.impl.BloomFilterService;
import com.bilibili.constant.RedisConstant;
import com.bilibili.result.CacheResult;
import com.bilibili.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class CacheClient {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private BloomFilterService bloomFilterService;

    /**
     * 逻辑过期
     *
     * @param keyPrefix
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String keyPrefix, String key, Object value, Long time, TimeUnit unit) {

    }

    /**
     * 设置缓存伴随随机时间
     * 防缓存雪崩
     * 解决: 大量key同时过期 → 雪崩
     *
     * @param keyPrefix
     * @param key
     * @param value
     * @param time
     */
    public void setWithRandomTTL(String keyPrefix, String key, Object value, Long time) {

        long radomTTL = time + new Random().nextInt(RedisConstant.REDIS_KEY_RADOM_EXPIRES_RANGE);
        redisUtil.set(keyPrefix, key, value, time);
    }

    /**
     * 查询
     * 防缓存穿透
     * 普通查询接口（用户 / 商品）
     *
     * @param keyPrefix  redis key前缀
     * @param id         主键
     * @param type       类型
     * @param dbFallback 查询函数
     * @param time       过期时间
     * @param <R>        查询函数返回类型
     * @param <ID>       查询函数参数
     * @return 查询结果
     */
    public <R, ID> R queryWithPassThrough(
            String keyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time
    ) {

        // 0.布隆过滤器拦截
        RBloomFilter<ID> bloomFilter = bloomFilterService.get(
                keyPrefix + "bloom",
                1000000,
                0.01
        );

        if (!bloomFilter.contains(id)) {
            return null;
        }

        CacheResult<R> cache = redisUtil.getCache(keyPrefix, id, type);

        // 1.缓存命中返回
        if (cache.isExist()) {
            if (cache.isEmpty()) {
                return null;
            }
            return cache.getData();
        }

        // 2.缓存未命中查询数据库
        R r = dbFallback.apply(id);

        // 2.1.数据库命中设置缓存并返回
        if (r != null) {
            // 添加随机过期时间解决缓存雪崩
            long radomTTL = time + new Random().nextInt(RedisConstant.REDIS_KEY_RADOM_EXPIRES_RANGE);
            redisUtil.set(keyPrefix, id, r, radomTTL);
            bloomFilter.add(id);
            return r;
        }

        // 2.2.数据库未命中设置空值并返回
        redisUtil.set(keyPrefix, id, "", RedisConstant.CACHE_NULL_TTL);

        return null;
    }

    /**
     * 防缓存击穿（互斥锁方案）
     * 解决: 热点key失效 → 大量请求打数据库（击穿）
     *
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        // 缓存未命中 → 加锁
        // 只允许一个线程查数据库
        // 其他线程等待
        return null;
    }

    public boolean tryLock(String key) {
        return false;
    }

    public void unlock(String key) {

    }

}
