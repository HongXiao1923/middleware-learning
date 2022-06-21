package com.debug.middleware.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月15日 14:35
 */
public class CommonConfig {
    //Redis链接工厂
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    //缓存操作组件RedisTemplate的自定义配置
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        //定义RedisTemplate工厂实例
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置Redis的链接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //指定Key序列化策略为String序列化，Value为JDK自带的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        //指定HashKey序列化策略为String序列化（针对Hash存储）
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
    //缓存操作组件StringRedisTemplate的自定义配置
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        //采用默认方法配置
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);

        return stringRedisTemplate;
    }
}
