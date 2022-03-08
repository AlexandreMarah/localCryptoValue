package com.alexandre.marah.localcryptovalue.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cache configuration manager.
 */
@Configuration
@EnableCaching
public class CacheConfigManager {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("crypto-value"));
        cacheManager.setCaffeine(caffeineCacheBuilder(500, 10));
        return cacheManager;
    }

    Caffeine< Object, Object > caffeineCacheBuilder(int maximumSize, int duration) {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(maximumSize)
                .expireAfterWrite(duration, TimeUnit.SECONDS)
                .recordStats();
    }

    @Bean
    CacheManager alternateCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("crypto-list"));
        cacheManager.setCaffeine(caffeineCacheBuilder(100, 600));
        return cacheManager;
    }
}
