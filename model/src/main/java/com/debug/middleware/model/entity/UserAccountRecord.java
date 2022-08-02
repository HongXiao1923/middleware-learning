package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/7/8 17:00
 */
@Data
@ToString
public class UserAccountRecord {
    //主键ID、账户记录主键ID、提现金额、提现成功时间
    private Integer id;
    private Integer accountId;
    private BigDecimal money;
    private Date createTime;
}
