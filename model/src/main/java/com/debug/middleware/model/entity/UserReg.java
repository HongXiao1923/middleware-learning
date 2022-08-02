package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户注册实体信息
 * @date 2022/7/21 21:23
 */
@Data
@ToString
public class UserReg {
    //用户ID、用户名、密码、创建时间
    private Integer id;
    private String userName;
    private String password;
    private Date createTime;
}
