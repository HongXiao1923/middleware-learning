package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 书籍抢购实体记录
 * @date 2022/7/27 18:14
 */
@Data
@ToString
public class BookRob {
    //主键ID、用户ID、书籍编号、抢购时间
    private Integer id;
    private Integer userId;
    private String bookNo;
    private Date robTime;
}
