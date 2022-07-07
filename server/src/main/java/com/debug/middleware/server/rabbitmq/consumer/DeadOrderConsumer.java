package com.debug.middleware.server.rabbitmq.consumer;

import com.debug.middleware.model.entity.UserOrder;
import com.debug.middleware.model.mapper.UserOrderMapper;
import com.debug.middleware.server.service.redis.DeadUserOrderService;
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
 * @date 2022/7/2 16:21
 */
@Component
public class DeadOrderConsumer {
    //日志、JSON组件
    private static final Logger log = LoggerFactory.getLogger(DeadOrderConsumer.class);
    private ObjectMapper objectMapper;
    //实例引入
    @Autowired(required = false)
    private UserOrderMapper userOrderMapper;
    @Autowired
    private DeadUserOrderService deadUserOrderService;

    /**
     * 用户支付超时消息模型——监听真正队列
     * @param orderId
     */
    @RabbitListener(queues = "${mq.consumer.order.real.queue.name}", containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload Integer orderId){
        try{
            log.info("用户下单支付超时消息模型-监听真正队列-监听到的内容为：orderId = {}", orderId);
            //核心业务逻辑
            //查询该用户下单记录ID对应的支付状态是否为“已保存”
            UserOrder userOrder = userOrderMapper.selectByIdAndStatus(orderId, 1);
            if(userOrder != null){
                //不等于null，则代表该用户下单记录仍然为“已保存”状态，即
                //该用户已经超时没有支付该笔订单，故而需要失效该笔下单记录
                deadUserOrderService.updateUserOrderRecord(userOrder);
            }
        }catch (Exception e){
            log.error("用户下单支付超时消息模型-监听真正队列-发生异常：orderId = {}", orderId, e.fillInStackTrace());
        }
    }

}
