package com.how2java.tmall.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Configuration
////Redis 缓存配置类
//public class RedisConfig extends CachingConfigurerSupport {
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        return cacheManager;
//
//    }
//}

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

//@EnableCaching
//@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
//public class RedisConfig extends CachingConfigurerSupport {
//
//    /**
//     * 配置自定义redisTemplate
//     *
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//
//        template.setConnectionFactory(connectionFactory);
//
//        template.setValueSerializer(jackson2JsonRedisSerializer());
//        //使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setStringSerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(jackson2JsonRedisSerializer());
//        template.afterPropertiesSet();
//
////        RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setKeySerializer(new StringRedisSerializer());
////        template.setHashKeySerializer(new StringRedisSerializer());
////        template.setHashValueSerializer(jdkSerializationRedisSerializer);
////        template.setValueSerializer(jdkSerializationRedisSerializer);
////        template.setConnectionFactory(connectionFactory);
////        template.afterPropertiesSet();
//
//        return template;
//    }
//
//    /**
//     * json序列化
//     * @return
//     */
//    @Bean
//    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        serializer.setObjectMapper(mapper);
//        return serializer;
//    }
//
//
//    /**
//     * 配置缓存管理器
//     * @param redisConnectionFactory
//     * @return
//     */
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//        // 设置缓存的默认过期时间，也是使用Duration设置
//        config = config.entryTtl(Duration.ofMinutes(5))
//                // 设置 key为string序列化
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                // 设置value为json序列化
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
//                // 不缓存空值
//                .disableCachingNullValues();
//        // 使用自定义的缓存配置初始化一个cacheManager
//        return RedisCacheManager
//                .builder(redisConnectionFactory)
//                .cacheDefaults(config)
//                .transactionAware()
//                .build();
//
//
//    }
//
//
//}

@Configuration
//Redis 缓存配置类
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        return redisCacheManager;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());

        return redisTemplate;
    }


    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}

