package com.debug.middleware.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 文件描述
 * 发红包随机金额明细实体类
 * @author dingguo.an
 * @date 2022年04月21日 21:56
 */
public class RedDetail {
    //主键
    private Integer id;
    //红包记录id
    private Integer recordId;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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
