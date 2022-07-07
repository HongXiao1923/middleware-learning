package com.debug.middleware.model.entity;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户下单实体
 * @date 2022/6/27 21:31
 */
public class UserOrder {
    //主键ID（用户下单记录ID）、订单号、用户ID、支付状态、是否有效、创建时间
    private Integer id;
    private String orderNo;
    private Integer userId;
    private Integer status;
    private Integer isActive;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
