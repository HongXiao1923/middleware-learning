package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.server.dto.UserLoginDto;
import com.debug.middleware.server.service.rabbitmq.SysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Arthur
 * @version 1.0
 * @description: 系统日志记录（消费者）
 * @date 2022/6/16 15:59
 */
@Component
public class LoginConsumer {
    //日志
    private static final Logger log = LoggerFactory.getLogger(LoginConsumer.class);
    //定义系统日志服务实例
    @Autowired
    private SysLogService sysLogService;

    /**
     * 监听消费并处理用户登录成功后的消息
     * @param loginDto
     */
    @RabbitListener(queues = "${mq.login.queue.name}", containerFactory = "singleListenerContainer")
    public void consumerMsg(@Payload UserLoginDto loginDto){
        try{
            log.info("系统日志记录-消费者-监听到消费用户登录成功后的消息-内容：{}", loginDto);
            //调用日志记录服务，用于记录用户登录成功后相关登录信息入数据库
            sysLogService.recordLog(loginDto);
        }catch (Exception e){
            log.error("系统日志记录-消费者-监听消费用户登录成功后的信息发生异常：{}", loginDto, e.fillInStackTrace());
        }
    }
}
