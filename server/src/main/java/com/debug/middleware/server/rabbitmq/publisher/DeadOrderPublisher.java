package com.debug.middleware.server.rabbitmq.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 死信队列用户下单支付超时消息模型——生产者
 * @date 2022/7/2 16:07
 */
@Component
public class DeadOrderPublisher {
    //日志
    private static final Logger log = LoggerFactory.getLogger(DeadOrderPublisher.class);
    //环境变量实例、RabbitMQ组件、JSON组件
    @Autowired
    private Environment env;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 将用户下单记录ID充当消息送入死信队列
     * @param orderId
     */
    public void sendMsg(Integer orderId){
        try{
            //设置消息格式为JSON
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            //设置基本交换机、基本路由
            rabbitTemplate.setExchange(env.getProperty("mq.producer.order.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.producer.order.routing.key.name"));
            //发送消息
            rabbitTemplate.convertAndSend(orderId, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //获取消息属性对象
                    MessageProperties messageProperties = message.getMessageProperties();
                    //设置消息的持久化模式
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    //设置消息头，即直接指定发送的消息所属的对象类型
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Integer.class);
                    return message;
                }
            });
            log.info("用户下单支付超时-发送用户下单记录ID的消息进入死信队列-内容为：orderId = {}", orderId);
        }catch (Exception e){
            log.error("用户下单支付超时-发送用户下单记录ID的消息进入死信队列-发生异常：orderId = {}", orderId, e.fillInStackTrace());
        }
    }
}
