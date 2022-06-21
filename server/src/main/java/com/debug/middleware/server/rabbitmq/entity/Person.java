package com.debug.middleware.server.rabbitmq.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月18日 11:09
 */
@Data
@ToString
public class Person implements Serializable {
    //声明可序列化字段
    private static final long serialVersionUID = -1;

    private Integer id;
    private String name;
    private String username;

    public Person() {
    }

    public Person(Integer id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }
}
