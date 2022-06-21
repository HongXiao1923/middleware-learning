package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.entity.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 *@ClassName BasicConsumer
 *@Description 基本消费模型（消费者）
 *@Author 鸿都
 *@Date 22/5/31 0031 21:34
 **/
@Component
public class BasicConsumer {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(BasicConsumer.class);
    //定义JSON序列化实例
    @Autowired
    public ObjectMapper objectMapper;

    /**
     * 监听并接受消费队列中的消息，在这里采用单一容器工厂实例即可
     * 由于消息本质是一串二进制数据流，故而监听接受的消息采用数组类型接受
     * @param msg
     */
    @RabbitListener(queues = "${mq.basic.info.queue.name}", containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload byte[] msg){
        try{
            //将字节数组的消息转化为字符串并输出
            String message = new String(msg, "utf-8");
            log.info("基本消息模型-消费者-监听消费者消息：{}", message);
        }catch (Exception e){
            log.error("基本消息模型-消费者-发生异常：", e.fillInStackTrace());
        }
    }

    /**
     * 监听并消费队列中的消息，监听消费处理对象信息，在这里采用单一容器工厂实例即可
     * @param person
     */
    @RabbitListener(queues = "${mq.object.info.queue.name}", containerFactory = "singleListenerContainer")
    public void consumerObjectMsg(@Payload Person person){
        try{
            log.info("基本消息模型-监听消费处理对象信息-消费者-监听消费到信息：{}", person);
        }catch (Exception e){
            log.error("基本消息模型-监听消费处理对象信息-消费者-发生异常：", e.fillInStackTrace());
        }
    }
}
