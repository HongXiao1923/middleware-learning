package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户实体提现申请实体
 * @date 2022/7/8 16:05
 */
@Data
@ToString
public class UserAccountDto implements Serializable {
    //用户ID、提现金额
    private Integer userId;
    private Double amount;
}
