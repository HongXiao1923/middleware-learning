package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月19日 13:46
 */
@Data
@ToString
public class Fruit implements Serializable {
    private String name;
    private String color;

    public Fruit() {
    }

    public Fruit(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
