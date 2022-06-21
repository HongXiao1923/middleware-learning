package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月15日 15:31
 */
@Data
@ToString
public class User {
    private Integer id;
    private String username;
    private String name;

    public User() {
    }

    public User(Integer id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}
