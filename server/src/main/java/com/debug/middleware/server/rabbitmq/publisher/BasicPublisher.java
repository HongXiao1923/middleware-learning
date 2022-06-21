package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.rabbitmq.entity.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *@ClassName BasicPublisher
 *@Description 基本消息模型（生产者）
 *@Author 鸿都
 *@Date 22/5/31 0031 21:14
 **/
@Component
public class BasicPublisher {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(BasicPublisher.class);
    //定义JSON序列化和反序列化实例
    private ObjectMapper objectMapper;
    //定义RabbitMQ消息操作组件
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //定义环境变量读取实例
    @Autowired
    private Environment env;

    /**
     * 发送消息
     * @param message 待发送的消息为一串字符串值
     */
    public void sendMsg(String message){
        //判断字符串值是否为空
        if(!Strings.isNullOrEmpty(message)){
            try{
                //定义消息传输的格式为JSON字符串格式
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //指定消息模型中的交换机
                rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"));
                //指定消息模型中的路由
                rabbitTemplate.setRoutingKey(env.getProperty("mq.basic.info.routing.key.name"));
                //将字符串值转为待发送的消息，即一串二进制数据流
                Message msg = MessageBuilder.withBody(message.getBytes("utf-8")).build();
                //转化并发送消息
                rabbitTemplate.convertAndSend(msg);
                //输出日志
                log.info("基本消息模型-生产者-发送消息：{}", message);
            }catch (Exception e){
                log.error("基本消息模型-生产者-发送消息发生异常：{}", message, e.fillInStackTrace());
            }
        }
    }
    public void sendObjectMsg(Person p){
        if(p != null){
            try{
                //指定消息的传输格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //指定消息发送时对应的交换机、路由
                rabbitTemplate.setExchange(env.getProperty("mq.object.info.exchange.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.object.info.routing.key.name"));
                //发送消息
                rabbitTemplate.convertAndSend(p, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //获取消息属性
                        MessageProperties messageProperties = message.getMessageProperties();
                        //设置消息持久化模式
                        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        //设置消息类型
                        messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Person.class);
                        //返回实例
                        return message;
                    }
                });
                //输出日志信息
                log.info("基本消息模型-生产者-发送对象类型的消息：{}", p);
            }catch (Exception e){
                log.error("基本消息模型-生产者-发送对象类型的消息发生异常：{}", p, e.fillInStackTrace());
            }
        }
    }
}
