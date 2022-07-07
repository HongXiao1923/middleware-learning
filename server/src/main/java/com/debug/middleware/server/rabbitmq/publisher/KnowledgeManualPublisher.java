package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.rabbitmq.entity.KnowledgeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 确认消费模式——手动确认消费（生产者）
 * @date 2022/6/13 17:16
 */
@Component
public class KnowledgeManualPublisher {
    //日志、JSON序列化组件、RabbitMQ操作组件、环境变量实例
    private static final Logger log = LoggerFactory.getLogger(KnowledgeManualPublisher.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Environment env;

    /**
     * 基于MANUAL机制，生产者发送消息
     * @param info
     */
    public void sendManualMsg(KnowledgeInfo info){
        try{
            if(info != null){
                //JSON消息格式、指定交换机、指定路由、创建消息并发送、日志输出
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.manual.knowledge.exchange.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.manual.knowledge.routing.key.name"));
                Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info))
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();
                rabbitTemplate.convertAndSend(message);
                log.info("基于MANUAL机制-生产者发送消息-内容为：{}", info);
            }
        }catch (Exception e){
            log.error("基于MANUAL机制-生产者发送消息-发送消息异常：{}", info, e.fillInStackTrace());
        }
    }

    /**
     * @author Arthur
     * @version 1.0
     * @description: 确认消费模式-生产者
     * @date 2022/6/12 17:05
     */
    @Component
    public static class KnowledgePublisher {
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

        /**
         * @author Arthur
         * @version 1.0
         * @description: 确认消费模式-消费者
         * @date 2022/6/12 17:17
         */
        @Component
        public static class KnowledgeConsumer {
            //日志、JSON序列化组件
            private static final Logger log = LoggerFactory.getLogger(KnowledgeConsumer.class);
            @Autowired
            private ObjectMapper objectMapper;

            /**
             * 基于AUTO的确认消费模式——消费者
             * 其中：queues指的是监听的队列，containerFactory指的是监听器所在的容器工厂
             * @param msg
             */
            @RabbitListener(queues = "${mq.auto.knowledge.queue.name}", containerFactory = "singleListenerContainerAuto")
            public void consumerAutoMsg(@Payload byte[] msg){
                try{
                    //监听消费解析消息体
                    KnowledgeInfo info = objectMapper.readValue(msg, KnowledgeInfo.class);
                    //输出日志
                    log.info("基于AUTO的确认模式-消费者监听消费消息-内容为：{}", info);
                }catch (Exception e){
                    log.error("基于AUTO的确认模式-消费者监听消费消息-发生异常：", e.fillInStackTrace());
                }
            }
        }
    }
}
