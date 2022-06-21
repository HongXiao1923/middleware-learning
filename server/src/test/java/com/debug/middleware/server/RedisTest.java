package com.debug.middleware.server;

import com.debug.middleware.server.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringTest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月15日 14:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    //定义日志
    public static final Logger log = LoggerFactory.getLogger(RedisTest.class);

    //定义RedisTemplate操作组件
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    //定义JSON序列化与反序列化框架类
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //采用RedisTemplate将一字符串信息写入缓存中，然后再读取出来
    @Test
    public void one(){
        log.info("---------开始RedisTemplate操作组件实战---------");
        final String content = "RedisTemplate实战字符串信息";
        final String key = "redis:template:one:string";
        //Redis通用的操作组件
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //将字符串信息写入缓存
        log.info("写入缓存的内容：{}", content);
        valueOperations.set(key, content);
        //从缓存中读取内容
        Object result = valueOperations.get(key);
        log.info("读取出来的内容为：{}", result);
    }

    //将对象序列化为JSON格式字符串后写入缓存
    @Test
    public void two() throws Exception {
        log.info("---------开始RedisTemplate操作组件实战---------");
        //构建对象信息
        User user = new User(1,"debug","阿修罗");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        final String key = "redis:template:two:string";
        final String content = objectMapper.writeValueAsString(user);
        valueOperations.set(key, content);
        log.info("写入缓存对象的信息：{}", user);
        Object result = valueOperations.get(key);
        if(result != null){
            User resultUser = objectMapper.readValue(result.toString(), User.class);
            log.info("读取缓存内容并反序列化后的结果：{}", resultUser);
        }
    }

    //采用StringRedisTemplate将一字符串信息写入缓存中，然后再读取出来
    @Test
    public void three(){
        log.info("---------开始StringRedisTemplate操作组件实战---------");
        final String content = "StringRedisTemplate实战字符串信息";
        final String key = "redis:three";
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        //将字符串写入缓存中
        log.info("写入缓存的内容：{}", content);
        valueOperations.set(key, content);
        //从缓存中读取内容
        Object result = valueOperations.get(key);
        log.info("从缓存中读取的内容为：{}", result);
    }

    //将对象序列化为JSON格式字符串后写入缓存
    @Test
    public void four() throws Exception{
        log.info("---------开始StringRedisTemplate操作组件实战---------");
        User user = new User(2, "SteadyJack", "阿修罗");
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        //写入缓存再读取
        final String key = "redis:four";
        final String content = objectMapper.writeValueAsString(user);
        valueOperations.set(key, content);
        log.info("写入缓存的内容为：{}", user);
        Object result = valueOperations.get(key);
        if(result != null){
            User resultUser = objectMapper.readValue(result.toString(), User.class);
            log.info("读取缓存内容并反序列化的结果为：{}", resultUser);
        }

    }

}
