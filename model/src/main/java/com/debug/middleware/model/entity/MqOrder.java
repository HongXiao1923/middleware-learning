package com.debug.middleware.model.entity;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 死信队列更新失效订单的状态实体
 * @date 2022/6/27 21:30
 */
public class MqOrder {
    //主键ID、下单记录ID、失效下单记录状态的业务时间、备注信息
    private Integer id;
    private Integer orderId;
    private Date businessTime;
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(Date businessTime) {
        this.businessTime = businessTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
