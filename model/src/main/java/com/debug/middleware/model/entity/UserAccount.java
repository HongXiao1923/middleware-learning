package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/7/8 16:58
 */
@Data
@ToString
public class UserAccount {
    //主键ID、用户ID、账户余额、版本号、是否为有效账户
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private Integer version;
    private Byte isActive;
}
