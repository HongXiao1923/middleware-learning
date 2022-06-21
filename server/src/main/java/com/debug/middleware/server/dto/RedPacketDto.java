package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 文件描述
 * 发红包请求时接受的参数对象
 * @author dingguo.an
 * @date 2022年04月23日 15:30
 */
@Data
@ToString
public class RedPacketDto {
    //用户id
    private Integer userId;
    //红包个数
    @NotNull
    private Integer total;
    //总金额，单位为分
    @NotNull
    private Integer amount;
}
