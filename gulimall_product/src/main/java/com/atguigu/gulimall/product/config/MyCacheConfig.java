package com.atguigu.gulimall.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存相关的配置
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)   //绑定
public class MyCacheConfig {

    /**
     * 1.原本和配置文件绑定的配置类是这样子
     *
     * @ConfigurationProperties( prefix = "spring.cache"
     * )
     * public class CacheProperties
     * 2.要让自定义的配置类绑定到CacheProperties
     * @EnableConfigurationProperties(CacheProperties.class)
     */

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 修改SpringCache的key,value机制 【将数据保存为json格式】
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 迁移配置文件中的配置项  【仿造写法：RedisCacheConfiguration】
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        //修改过期时间 TTL
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        //修改KEY类型
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        //修改value类型
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        //密钥
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }


        return config;
    }

}
