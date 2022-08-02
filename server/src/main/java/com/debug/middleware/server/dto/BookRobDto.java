package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Arthur
 * @version 1.0
 * @description: 书籍抢购实体
 * @date 2022/7/27 19:08
 */
@Data
@ToString
public class BookRobDto implements Serializable {
    //用户ID、书籍编号
    private Integer userId;
    private String bookNo;
}
