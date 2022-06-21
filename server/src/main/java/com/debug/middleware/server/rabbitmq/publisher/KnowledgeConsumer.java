package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.rabbitmq.entity.KnowledgeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 确认消费模式-消费者
 * @date 2022/6/12 17:17
 */
@Component
public class KnowledgeConsumer {
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
