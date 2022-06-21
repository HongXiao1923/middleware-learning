package com.debug.middleware.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 文件描述
 * 抢红包时金额等相关信息记录表
 * @author dingguo.an
 * @date 2022年04月21日 21:59
 */
public class RedRobRecord {
    //主键
    private Integer id;
    private Integer userId;
    //红包全局唯一标识
    private String redPacket;
    private BigDecimal amount;
    private Date robTime;
    private Byte isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getRobTime() {
        return robTime;
    }

    public void setRobTime(Date robTime) {
        this.robTime = robTime;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}
