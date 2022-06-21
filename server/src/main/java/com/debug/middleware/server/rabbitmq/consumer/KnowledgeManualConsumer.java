package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.rabbitmq.entity.KnowledgeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 确认消费模式——手动确认消费（监听器、消费者）
 * @date 2022/6/13 17:34
 */
@Component("knowledgeManualConsumer")
public class KnowledgeManualConsumer implements ChannelAwareMessageListener {
    //日志、JSON序列化组件
    private static final Logger log = LoggerFactory.getLogger(KnowledgeManualConsumer.class);
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 监听消费消息
     * @param message 消息实体
     * @param channel 通道实例
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //获取消息属性
        MessageProperties messageProperties = message.getMessageProperties();
        //获取消息分发时的全局唯一标识
        long deliveryTag = messageProperties.getDeliveryTag();
        try{
            //获取消息体
            byte[] msg = message.getBody();
            //解析消息体
            KnowledgeInfo info = objectMapper.readValue(msg, KnowledgeInfo.class);
            log.info("确认消费模式-手动确认消费-监听器监听消费消息-内容为：{}", info);
            //执行完业务逻辑之后，手动确认消费
            //第1个参数：消息的分支标识（全局唯一）；第2个参数：是否允许批量确认消费
            channel.basicAck(deliveryTag, true);
        }catch (Exception e){
            log.error("确认消费模式-手动确认消费-监听器监听消费消息-发生异常：", e.fillInStackTrace());
            //如果在处理消息的过程中发生了异常，则照样需要人为手动确认消费该消息
            //否则该消息将一直留在队列中，从而将导致消息的重复消费
            channel.basicReject(deliveryTag, false);
        }
    }
}
