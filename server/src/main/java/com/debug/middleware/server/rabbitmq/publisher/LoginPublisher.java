package com.debug.middleware.server.rabbitmq.publisher;

import com.debug.middleware.server.dto.UserLoginDto;
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
 * @description: 系统日志记录（生产者）
 * @date 2022/6/16 15:45
 */
@Component
public class LoginPublisher {
    //日志
    private static final Logger log = LoggerFactory.getLogger(LoginPublisher.class);
    //定义RabbitMQ组件、环境变量实例、JSON序列化组件
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 发送登录成功后的用户相关信息入队列
     * @param loginDto
     */
    public void sendLogMsg(UserLoginDto loginDto){
        try{
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.login.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.login.routing.key.name"));
            //发送消息
            rabbitTemplate.convertAndSend(loginDto, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //获取消息属性
                    MessageProperties messageProperties = message.getMessageProperties();
                    //设置消息持久化模式为持久化
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    //设置消息头，
                    //表示传输的消息直接指定为某个类实例，消费者在监听时可以直接定义该类对象参数进行直接接受即可
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, UserLoginDto.class);

                    return message;
                }
            });
            log.info("系统日志记录-生产者-发送登录成功后的用户相关信息入队列内容：{}", loginDto);
        }catch (Exception e){
            log.error("系统日志记录-生产者-发送登录成功后的用户相关信息入队列发生异常：{}", loginDto, e.fillInStackTrace());
        }
    }
}
