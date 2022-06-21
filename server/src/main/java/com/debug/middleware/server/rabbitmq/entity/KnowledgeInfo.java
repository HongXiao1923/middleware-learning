package com.debug.middleware.server.rabbitmq.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/6/12 16:43
 */
@Data
@ToString
public class KnowledgeInfo implements Serializable {
    //id标识、模式名称、对应编码
    private Integer id;
    private String mode;
    private String code;
}
