package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户注册请求接受的信息封装的中间实体
 * @date 2022/7/21 21:44
 */
@Data
@ToString
public class UserRegDto implements Serializable {
    //用户名、密码
    private String userName;
    private String password;
}
