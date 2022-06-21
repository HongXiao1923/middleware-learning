package com.debug.middleware.server;

import com.debug.middleware.server.entity.Fruit;
import com.debug.middleware.server.entity.Person;
import com.debug.middleware.server.entity.PhoneUser;
import com.debug.middleware.server.entity.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月18日 11:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest2 {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedisTest2.class);
    //定义RedisTemplete操作组件
    @Autowired
    private RedisTemplate redisTemplate;
    //定义JSON序列化与反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void one() throws Exception{
        //构建实体
        Person person = new Person(10013,23,"修罗","debug","太阳系");
        //缓存操作
        final String key = "redis:test:1";
        String value = objectMapper.writeValueAsString(person);
        log.info("存入缓存的实体用户信息为：{}", person);
        redisTemplate.opsForValue().set(key,value);
        Object result = redisTemplate.opsForValue().get(key);
        if(result != null){
            Person p = objectMapper.readValue(result.toString(), Person.class);
            log.info("从缓存中读取的信息为：{}", p);
        }
    }

    @Test
    public void two() throws Exception{
        //构建已排好序的用户列表
        List<Person> list = new ArrayList<>();
        list.add(new Person(1,18,"修罗","debug","火星"));
        list.add(new Person(2,19,"悟空","wukong","花果山"));
        list.add(new Person(3,20,"嬴政","qin","秦朝"));
        log.info("构建已排好序的用户对象：{}", list);
        final String key = "redis:test:2";
        ListOperations listOperations = redisTemplate.opsForList();
        for(Person p : list){
            //从队尾添加数据
            listOperations.leftPush(key,p);
        }
        log.info("---------从队头读取数据----------");
        Object res = listOperations.rightPop(key);
        Person person;
        while(res != null){
            person = (Person) res;
            log.info("当前数据为：{}",person);
            res = listOperations.rightPop(key);
        }
    }

    @Test
    public void three() throws Exception{
        List<String> userList = new ArrayList<>();
        userList.add("debug");
        userList.add("jack");
        userList.add("大圣");
        userList.add("修罗");
        userList.add("Steven11923");
        userList.add("debug");
        userList.add("jack");
        userList.add("大圣");
        userList.add("修罗");

        log.info("待处理的用户名单：{}", userList);
        //缓存操作
        final String key = "redis:test:3";
        SetOperations setOperations = redisTemplate.opsForSet();
        for(String str : userList){
            setOperations.add(key, str);
        }
        Object res = setOperations.pop(key);
        while(res != null){
            log.info("从缓存中读取数据---当前用户为：{}", res);
            res = setOperations.pop(key);
        }
    }

    @Test
    public void four(){
        List<PhoneUser> list = new ArrayList<>();
        list.add(new PhoneUser("103",130.0));
        list.add(new PhoneUser("121", 180.0));
        list.add(new PhoneUser("189",200.0));
        list.add(new PhoneUser("190",60.0));
        list.add(new PhoneUser("150",50.0));
        list.add(new PhoneUser("166",90.0));

        log.info("一组无序的手机用户充值对象：{}", list);
        final String key = "redis:test:4";
        //这里为了测试方便，先清空缓存（实际应用中不建议这么做）
        redisTemplate.delete((key));
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        for(PhoneUser u : list){
            zSetOperations.add(key,u,u.getFare());
        }
        //前端访问充值列表靠前的列表
        Long size = zSetOperations.size(key);
        //从小到大排序
        Set<PhoneUser> resSet = zSetOperations.range(key,0L,size);
        //从大到小排序
        //Set<PhoneUser> resSet = zSetOperations.reverseRange((key,0L,size);
        for(PhoneUser u: resSet){
            log.info("从缓存中读取的用户的手机充值记录排序列表---当前记录：{}",u);
        }
    }

    @Test
    public void five(){
        //构建两个对象列表
        List<Student> students = new ArrayList<>();
        List<Fruit> fruits = new ArrayList<>();
        students.add(new Student("1001","debug","修罗"));
        students.add(new Student("1002","jack","大圣"));
        students.add(new Student("1003","sam","上古"));
        fruits.add(new Fruit("apple","红色"));
        fruits.add(new Fruit("orange","橙色"));
        fruits.add(new Fruit("banana","黄色"));
        final String sKey = "redis:test:5:1";
        final String fKey = "redis:test:5:2";
        //哈希组件
        HashOperations hashOperations = redisTemplate.opsForHash();
        for(Student s: students){
            hashOperations.put(sKey,s.getId(),s);
        }
        for(Fruit f: fruits){
            hashOperations.put(fKey,f.getName(),f);
        }
        //获取对象的对应列表
        Map<String,Student> sMap = hashOperations.entries(sKey);
        Map<String,Fruit> fMap = hashOperations.entries(fKey);
        log.info("获取的学生对象的列表为：{}",sMap);
        log.info("获取的水果对象的列表为：{}",fMap);
        //获取指定对象
        String sField = "1002";
        Student stu = (Student) hashOperations.get(sKey, sField);
        log.info("获取指定的学生对象为：{} -> {}",sField,stu);
        String fField = "orange";
        Fruit fru = (Fruit) hashOperations.get(fKey, fField);
        log.info("获取指定的水果对象为：{} -> {}",fField,fru);
    }

    //测试Key是否失效，方式一
    @Test
    public void six() throws Exception{
        final String key = "redis:test:6";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //设置过期时间TTL为10秒
        valueOperations.set(key,"expire操作",10L, TimeUnit.SECONDS);
        //等待5秒，判断可以是否还存在
        Thread.sleep(5000);
        Boolean existKey = redisTemplate.hasKey(key);
        Object value = valueOperations.get(key);
        log.info("等待5秒，判断key是否还存在：{}，对应的值为：{}",existKey,value);
        //再等5秒，判断是否还存在
        Thread.sleep(5000);
        existKey = redisTemplate.hasKey(key);
        value = valueOperations.get(key);
        log.info("再等待5秒，判断key是否还存在：{}，对应的值为：{}",existKey,value);
    }

    //测试Key是否失效，方式二
    @Test
    public void seven() throws Exception{
        final String key = "redis:test:7";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,"expire操作--2");
        redisTemplate.expire(key,10L,TimeUnit.SECONDS);
        //等待5秒，判断可以是否还存在
        Thread.sleep(5000);
        Boolean existKey = redisTemplate.hasKey(key);
        Object value = valueOperations.get(key);
        log.info("等待5秒，判断key是否还存在：{}，对应的值为：{}",existKey,value);
        //再等5秒，判断是否还存在
        Thread.sleep(5000);
        existKey = redisTemplate.hasKey(key);
        value = valueOperations.get(key);
        log.info("再等待5秒，判断key是否还存在：{}，对应的值为：{}",existKey,value);
    }

}
