package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.EventInfo;
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
 * @description: TODO
 * @date 2022/6/7 20:57
 */
@Component
public class ModelConsumer {
    //日志
    private static final Logger log = LoggerFactory.getLogger(ModelConsumer.class);
    //JSON序列化组件
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 监听并消费队列中的消息fanoutExchange-one, 这是第1条队列对应的消费者
     * @param msg
     */
    @RabbitListener(queues = "${mq.fanout.queue.one.name}", containerFactory = "singleListenerContainer")
    public void consumerFanoutMsgOne(@Payload byte[] msg){
        try{
            //监听消息队列中的消息，并进行解析处理
            EventInfo info = objectMapper.readValue(msg, EventInfo.class);
            log.info("消息模型fanoutExchange-one-消费者-监听已消费到消息：{}", info);
        }catch (Exception e){
            log.error("消息模型fanoutExchange-one-消费者-发生异常：", e.fillInStackTrace());
        }
    }
    /**
     * 监听并消费队列中的消息fanoutExchange-two, 这是第2条队列对应的消费者
     * @param msg
     */
    @RabbitListener(queues = "${mq.fanout.queue.two.name}", containerFactory = "singleListenerContainer")
    public void consumerFanoutTwo(@Payload byte[] msg){
        try{
            //监听消费队列中的消息，并进行解析处理
            EventInfo info = objectMapper.readValue(msg, EventInfo.class);
            log.info("消息模型fanoutExchange-two-消费者-监听已消费到的消息：{}", info);
        }catch (Exception e){
            log.error("消息模型fanoutExchange-two-消费者-发生异常：", e.fillInStackTrace());
        }
    }

    /**
     * 基于DirectExchange消息模型（one）
     * 第一个路由绑定对应的队列的消费方法
     * 监听并消费队列中的消息
     * @param msg
     */
    @RabbitListener(queues = "${mq.direct.queue.one.name}", containerFactory = "singleListenerContainer")
    public void consumerDirectMsgOne(@Payload byte[] msg){
        try{
            //监听消息并进行JSON反序列化解析
            EventInfo info = objectMapper.readValue(msg, EventInfo.class);
            //输出日志
            log.info("消息模型 DirectExchange-one-消费者-监听已消费到的消息：{}", info);
        }catch (Exception e){
            log.error("消息模型 DirectExchange-one-消费者-监听消费发生异常：", e.fillInStackTrace());
        }
    }
    /**
     * 基于DirectExchange消息模型（one）
     * 第二个路由绑定对应的队列的消费方法
     * 监听并消费队列中的消息
     * @param msg
     */
    @RabbitListener(queues = "${mq.direct.queue.two.name}", containerFactory = "singleListenerContainer")
    public void consumerDirectMsgTwo(@Payload byte[] msg){
        try{
            //监听消息并进行JSON反序列化解析
            EventInfo info = objectMapper.readValue(msg, EventInfo.class);
            //输出日志
            log.info("消息模型 DirectExchange-two-消费者-监听已消费到的消息：{}", info);
        }catch (Exception e){
            log.error("消息模型 DirectExchange-two-消费者-监听消费发生异常：", e.fillInStackTrace());
        }
    }

    /**
     * 监听消费队列中的消息——TopicExchange（通配符*）
     * @param msg
     */
    @RabbitListener(queues = "${mq.topic.queue.one.name}", containerFactory = "singleListenerContainer")
    public void consumerTopicMsgOne(@Payload byte[] msg){
        try{
            //监听消费者并进行解析
            String message = new String(msg, "utf-8");
            log.info("消息模型 TopicExchange - * - 消费者 -监听已消费到的消息：{}", message);
        }catch (Exception e){
            log.error("消息模型 TopicExchange - * - 消费者 -监听消费发生异常：", e.fillInStackTrace());
        }
    }
    /**
     * 监听消费队列中的消息——TopicExchange（通配符#）
     * @param msg
     */
    @RabbitListener(queues = "${mq.topic.queue.two.name}", containerFactory = "singleListenerContainer")
    public void consumerTopicMsgTwo(@Payload byte[] msg){
        try{
            //监听消费者并进行解析
            String message = new String(msg, "utf-8");
            log.info("消息模型 TopicExchange - # - 消费者 -监听已消费到的消息：{}", message);
        }catch (Exception e){
            log.error("消息模型 TopicExchange - # - 消费者 -监听消费发生异常：", e.fillInStackTrace());
        }
    }
}
