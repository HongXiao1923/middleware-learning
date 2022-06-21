package com.debug.middleware.server.rabbitmq.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: 实体对象信息
 * @date 2022/6/7 20:38
 */
@Data
@ToString
public class EventInfo implements Serializable {
    //标识、模块、名称、描述
    private Integer id;
    private String module;
    private String name;
    private String desc;

    public EventInfo() {
    }

    public EventInfo(Integer id, String module, String name, String desc) {
        this.id = id;
        this.module = module;
        this.name = name;
        this.desc = desc;
    }
}
