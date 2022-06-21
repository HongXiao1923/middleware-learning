package com.debug.middleware.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户实体信息
 * @date 2022/6/14 17:31
 */
@Data
public class User {
    //用户ID、用户名、密码、创建时间
    private Integer id;
    private String userName;
    private String password;
    private Date createTime;
}
