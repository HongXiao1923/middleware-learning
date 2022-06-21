package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.rabbitmq.entity.EventInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 消息模型（生产者）
 * @date 2022/6/7 20:45
 */
@Component
public class ModelPublisher {
    //日志
    private static final Logger log = LoggerFactory.getLogger(ModelPublisher.class);
    //JSON序列化组件
    @Autowired
    private ObjectMapper objectMapper;
    //消息组件
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //环境实例
    @Autowired
    private Environment env;

    /**
     * 基于 FanoutExchange 的消息模型
     * 发送消息
     * @param info
     */
    public void sendMsg(EventInfo info){
        if(info != null){
            try{
                //定义消息传输的格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //设置广播式交换机FanoutExchange
                rabbitTemplate.setExchange(env.getProperty("mq.fanout.exchange.name"));
                //消息实例
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
                //发送消息
                rabbitTemplate.convertAndSend(msg);
                //输出日志
                log.info("消息模型fanoutExchange-生产者-发送消息：{}", info);
            }catch (Exception e){
                log.error("消息模型fanoutExchange-生产者-发送消息发生异常：{}", info, e.fillInStackTrace());
            }
        }
    }

    /**
     * 基于 DirectExchange 的消息模型(one)
     * 发送消息
     * @param info
     */
    public void sendMsgDirectOne(EventInfo info){
        if(info != null){
            try{
                //设置消息传输格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //设置交换机
                rabbitTemplate.setExchange(env.getProperty("mq.direct.exchange.name"));
                //设置路由1
                rabbitTemplate.setRoutingKey(env.getProperty("mq.direct.routing.key.one.name"));
                //创建消息并发送
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
                rabbitTemplate.convertAndSend(msg);
                //输出日志
                log.info("消息模型DirectExchange-one-生产者-发送消息：{}", info);
            }catch (Exception e){
                log.error("消息模型DirectExchange-one-生产者-发送消息异常：{}", info, e.fillInStackTrace());
            }
        }
    }
    /**
     * 基于 DirectExchange 的消息模型(two)
     * 发送消息
     * @param info
     */
    public void sendMsgDirectTwo(EventInfo info){
        if(info != null){
            try{
                //设置消息传输格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //设置交换机
                rabbitTemplate.setExchange(env.getProperty("mq.direct.exchange.name"));
                //设置路由1
                rabbitTemplate.setRoutingKey(env.getProperty("mq.direct.routing.key.two.name"));
                //创建消息并发送
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
                rabbitTemplate.convertAndSend(msg);
                //输出日志
                log.info("消息模型DirectExchange-two-生产者-发送消息：{}", info);
            }catch (Exception e){
                log.error("消息模型DirectExchange-two-生产者-发送消息异常：{}", info, e.fillInStackTrace());
            }
        }
    }

    /**
     * 发送消息，基于TopicExchange消息模型
     * @param msg
     * @param routingKey
     */
    public void sendMsgTopic(String msg, String routingKey){
        if(!Strings.isNullOrEmpty(msg) && !Strings.isNullOrEmpty(routingKey)){
            try{
                //设置消息传输格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //指定交换机
                rabbitTemplate.setExchange(env.getProperty("mq.topic.exchange.name"));
                //指定路由，根据通配符指定到对应的队列
                rabbitTemplate.setRoutingKey(routingKey);
                //创建并发送消息
                Message message = MessageBuilder.withBody(msg.getBytes("utf-8"))
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
                rabbitTemplate.convertAndSend(message);
                //输出日志
                log.info("消息模型TopicExchange-生产者-发送消息：{}，路由：{}", msg, routingKey);
            }catch (Exception e){
                log.error("消息模型TopicExchange-生产者-发送消息异常：{}", msg, e.fillInStackTrace());
            }
        }
    }
}
