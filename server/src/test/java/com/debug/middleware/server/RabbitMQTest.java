package com.debug.middleware.server;

import com.debug.middleware.server.rabbitmq.entity.DeadInfo;
import com.debug.middleware.server.rabbitmq.entity.EventInfo;
import com.debug.middleware.server.rabbitmq.entity.KnowledgeInfo;
import com.debug.middleware.server.rabbitmq.entity.Person;
import com.debug.middleware.server.rabbitmq.publisher.BasicPublisher;
import com.debug.middleware.server.rabbitmq.publisher.DeadPublisher;
import com.debug.middleware.server.rabbitmq.publisher.KnowledgeManualPublisher;
import com.debug.middleware.server.rabbitmq.publisher.ModelPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName RabbitMQTest
 * @Description TODO
 * @Author Arthur
 * @Date 22/6/1 0001 20:11
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RabbitMQTest {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RabbitMQTest.class);
    //定义JSON序列化实例
    @Autowired
    private ObjectMapper objectMapper;
    //定义基本消息模型中的发送消息的生产者实例
    @Autowired
    private BasicPublisher basicPublisher;
    //定义模板对象的生产者实例
    @Autowired
    private ModelPublisher modelPublisher;
    //定义自动确认消费的生产者实例
    @Autowired
    private KnowledgeManualPublisher.KnowledgePublisher knowledgePublisher;
    //定义手动确认消费的生产者实例
    @Autowired
    private KnowledgeManualPublisher knowledgeManualPublisher;
    //定义死信队列生产者实例
    @Autowired
    private DeadPublisher deadPublisher;

    /**
     * 用于发送消息的测试方法
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        //定义字符串值
        String msg = "----这是一串字符串消息----";
        //生产者实例发送消息
        basicPublisher.sendMsg(msg);
    }

    /**
     * 测试对象消息的发送
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        //构建人员实体对象信息
        Person p = new Person(1, "大圣", "Arthur");
        basicPublisher.sendObjectMsg(p);
    }

    /**
     * 测试对象消息的发送-FanoutExchange
     * @throws Exception
     */
    @Test
    public void test3() throws Exception{
        //构建实体对象信息
        EventInfo info = new EventInfo(1, "增删改查模板", "基于fanoutExchange的消息模型", "这是一个基于fanoutExchange的消息模型的生产者测试实例");
        modelPublisher.sendMsg(info);
    }
    /**
     * 测试对象消息的发送-DirectExchange
     * @throws Exception
     */
    @Test
    public void test4() throws Exception{
        //构建第1个实体信息并发送消息
        EventInfo info = new EventInfo(1,"增删改查模块-1","基于DirectExchange的消息模型-1","directExchagne-1");
        modelPublisher.sendMsgDirectOne(info);
        //构建第2个实体信息并发送消息
        info = new EventInfo(2,"增删改查模块-2","基于DirectExchange的消息模型-2","directExchagne-2");
        modelPublisher.sendMsgDirectTwo(info);
    }

    /**
     * 测试对象消息的发送-TopicExchange
     * @throws Exception
     */
    @Test
    public void test5() throws Exception{
        //定义待发送的消息，即一串字符串
        String msg = "这是TopicExchange消息模型的消息";
        //定义测试路由
        String routingKeyOne = "local.middleware.mq.topic.routing.java.key";
        String routingKeyTwo = "local.middleware.mq.topic.routing.php.python.key";
        String routingKeyThree = "local.middleware.mq.topic.routing.key";
        //分批测试，也可以同步测试
        modelPublisher.sendMsgTopic(msg, routingKeyOne);
        modelPublisher.sendMsgTopic(msg, routingKeyTwo);
        modelPublisher.sendMsgTopic(msg, routingKeyThree);
    }

    /**
     * 测试自动确认消费-AUTO
     * @throws Exception
     */
    @Test
    public void test6() throws Exception{
        //定义实体并指定消息
        KnowledgeInfo info = new KnowledgeInfo();
        info.setId(10010);
        info.setCode("auto");
        info.setMode("基于AUTO的消息确认消费模式");
        knowledgePublisher.sendAutoMsg(info);
    }

    /**
     * 测试手动确认消费-MANUAL
     * @throws Exception
     */
    @Test
    public void test7() throws Exception{
        //定义实体并指定消息
        KnowledgeInfo info = new KnowledgeInfo();
        info.setId(10020);
        info.setCode("manual");
        info.setMode("基于MANUAL的消息确认消费模式");
        knowledgeManualPublisher.sendManualMsg(info);
    }

    /**
     * 死信队列消费模型生产者实例
     * @throws Exception
     */
    @Test
    public void test8() throws Exception{
        //定义实体消息
        DeadInfo info = new DeadInfo(1, "~~~这是第一则消息~~~");
        deadPublisher.sendMsg(info);
        info = new DeadInfo(2, "~~~这是第二则消息~~~");
        deadPublisher.sendMsg(info);
        //等待20s再介绍，目的是为了能看到消费者监听消费真正队列中的消息
        Thread.sleep(20000);
    }
}
