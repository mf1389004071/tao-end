package com.geek.redis.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.geek.common.core.cache.TtlCacheManager;
import com.geek.common.utils.StringUtils;

public class RedisCacheManagerProxy implements TtlCacheManager {

    RedisCacheManager redisCacheManager;
    RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheManagerProxy(RedisCacheManager redisCacheManager, RedisTemplate<Object, Object> redisTemplate) {
        this.redisCacheManager = redisCacheManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisTemplate.opsForValue().set(cacheName + ":" + key, value);
                }
            });
            return;
        }

        redisTemplate.opsForValue().set(cacheName + ":" + key, value);
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value, long timeout, TimeUnit timeUnit) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisTemplate.opsForValue().set(cacheName + ":" + key, value, timeout, timeUnit);
                }
            });
            return;
        }

        redisTemplate.opsForValue().set(cacheName + ":" + key, value, timeout, timeUnit);
    }

    @Override
    public Set<String> getCachekeys(Cache cache) {
        Set<String> keyset = new HashSet<>();
        Set<Object> keysets = redisTemplate.keys(cache.getName() + "*");
        for (Object s : keysets) {
            keyset.add(StringUtils.replace(s.toString(), cache.getName() + ":", ""));
        }
        return keyset;
    }

    @Override
    public Cache getCache(String arg0) {
        return redisCacheManager.getCache(arg0);
    }

    @Override
    public Collection<String> getCacheNames() {
        return redisCacheManager.getCacheNames();
    }
}
