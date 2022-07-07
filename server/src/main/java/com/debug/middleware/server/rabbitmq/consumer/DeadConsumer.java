package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.DeadInfo;
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
 * @description: 死信队列-真正队列的消费者
 * @date 2022/6/26 20:42
 */
@Component
public class DeadConsumer {
    //日志、JSON组件
    private static final Logger log = LoggerFactory.getLogger(DeadConsumer.class);
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 监听真正队列——消费队列中的消息，面向消费者
     * @param info
     */
    @RabbitListener(queues = "${mq.consumer.real.queue.name}", containerFactory = "singleListenerContainer")
    public void consumerMsg(@Payload DeadInfo info){
        try{
            log.info("死信队列-监听真正队列-消费队列中的消息，监听到消息内容为：{}", info);
            //TODO 后续业务逻辑
        }catch (Exception e){
            log.error("死信队列-监听真正队列-消费队列中的消息，监听发生异常：{}", info, e.fillInStackTrace());
        }
    }
}
