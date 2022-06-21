package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/6/15 19:14
 */
@Data
@ToString
public class UserLoginDto implements Serializable {
    //用户名（必填）、密码（必填）、用户ID
    @NotBlank
    private String userName;
    @NotBlank
    private String password;

    private Integer userId;
}
