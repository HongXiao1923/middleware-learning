package com.debug.middleware.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 文件描述
 * 发红包记录实体类
 * @author dingguo.an
 * @date 2022年04月21日 21:53
 */
public class RedRecord {
    //主键
    private Integer id;
    private Integer userId;
    //红包的全局唯一标识
    private String redPacket;
    private Integer total;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
