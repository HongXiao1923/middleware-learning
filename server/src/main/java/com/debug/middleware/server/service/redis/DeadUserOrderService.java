package com.debug.middleware.server.service.redis;

import com.debug.middleware.model.entity.MqOrder;
import com.debug.middleware.model.entity.UserOrder;
import com.debug.middleware.model.mapper.MqOrderMapper;
import com.debug.middleware.model.mapper.UserOrderMapper;
import com.debug.middleware.server.dto.UserOrderDto;
import com.debug.middleware.server.rabbitmq.publisher.DeadOrderPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户下单支付超时（服务处理），两大核心功能：1）用户下单，2）更新用户下单的记录状态
 * @date 2022/7/1 15:41
 */
@Service
public class DeadUserOrderService {
    //日志
    private static final Logger log = LoggerFactory.getLogger(DeadUserOrderService.class);
    //定义用户下单操作接口
    @Autowired(required = false)
    private UserOrderMapper userOrderMapper;
    //定义更新失效用户下单记录状态接口Mapper
    @Autowired(required = false)
    private MqOrderMapper mqOrderMapper;
    //死信队列生产者实例
    @Autowired
    private DeadOrderPublisher deadOrderPublisher;

    /**
     * 用户下单，并将生成的下单记录ID加入死信队列等待延迟处理
     * @param userOrderDto
     * @throws Exception
     */
    public void pushUserOrder(UserOrderDto userOrderDto) throws Exception{
        //创建用户下单实例
        UserOrder userOrder = new UserOrder();
        //复制userOrderDto对应的字段取值到新的实例对象userOrder中
        BeanUtils.copyProperties(userOrderDto, userOrder);
        //设置支付状态为已保存
        userOrder.setStatus(1);
        //设置下单时间
        userOrder.setCreateTime(new Date());
        //插入用户下单记录
        userOrderMapper.insertSelective(userOrder);
        log.info("用户下单成功，下单信息为：{}", userOrder);
        //生成用户下单记录ID
        Integer orderId = userOrder.getId();
        //将生成的用户下单记录ID加入死信队列等待延迟处理
        deadOrderPublisher.sendMsg(orderId);
    }

    /**
     * 更新用户下单记录的状态
     * @param userOrder
     */
    public void updateUserOrderRecord(UserOrder userOrder){
        try{
            if(userOrder != null){
                //更新失效用户下单记录
                userOrder.setIsActive(0);
                //设置失效时进行更新的时间
                userOrder.setUpdateTime(new Date());
                //更新下单记录实体信息
                userOrderMapper.updateByPrimaryKeySelective(userOrder);

                //记录“失效用户下单记录”历史
                MqOrder mqOrder = new MqOrder();
                //设置失效时间
                mqOrder.setBusinessTime(new Date());
                //设置备注信息
                mqOrder.setMemo("更新失效当前用户下单记录 id, orderId = " + userOrder.getId());
                //设置下单记录ID
                mqOrder.setOrderId(userOrder.getId());
                //插入失效记录
                mqOrderMapper.insertSelective(mqOrder);
            }
        }catch (Exception e){
            log.error("用户下单支付超时-处理服务-更新用户下单记录的状态发生异常：", e.fillInStackTrace());
        }
    }
}
