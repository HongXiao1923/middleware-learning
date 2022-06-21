package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.KnowledgeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @description: 确认消费模式-生产者
 * @date 2022/6/12 17:05
 */
@Component
public class KnowledgePublisher {
    //日志、JSON序列化组件、RabbitMQ操作组件、环境变量实例
    private static final Logger log = LoggerFactory.getLogger(KnowledgePublisher.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Environment env;

    /**
     * 基于AUTO机制，生产者发送消息
     * @param info
     */
    public void sendAutoMsg(KnowledgeInfo info){
        try{
            if(info != null){
                //设置消息的传输格式为JSON
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                //设置交换机、路由
                rabbitTemplate.setExchange(env.getProperty("mq.auto.knowledge.exchange.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.auto.knowledge.routing.key.name"));
                //创建消息，其中对消息设置了持久化模式
                Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info))
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();
                //发送消息并输出日志
                rabbitTemplate.convertAndSend(message);
                log.info("基于AUTO机制-生产者发送消息-内容为：{}", info);
            }
        }catch (Exception e){
            log.error("基于AUTO机制-生产者发送消息-发生异常：{}", info, e.fillInStackTrace());
        }
    }
}
