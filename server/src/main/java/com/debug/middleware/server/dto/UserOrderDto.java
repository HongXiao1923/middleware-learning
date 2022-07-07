package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户下单实体信息
 * @date 2022/7/1 15:38
 */
@Data
@ToString
public class UserOrderDto implements Serializable {
    //订单编号、用户ID（均为必填）
    @NotBlank
    private String orderNo;
    @NotNull
    private Integer userId;
}
