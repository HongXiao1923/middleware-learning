package com.debug.middleware.server.rabbitmq.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Arthur
 * @version 1.0
 * @description: 死信队列实体对象信息
 * @date 2022/6/25 22:02
 */
@Data
@ToString
public class DeadInfo {
    //标识、描述信息
    private Integer id;
    private String msg;

    public DeadInfo() {
    }

    public DeadInfo(Integer id, String msg) {
        this.id = id;
        this.msg = msg;
    }
}
