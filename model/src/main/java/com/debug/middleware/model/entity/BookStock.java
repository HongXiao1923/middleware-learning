package com.debug.middleware.model.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Arthur
 * @version 1.0
 * @description: 数据库存记录实体
 * @date 2022/7/27 18:12
 */
@Data
@ToString
public class BookStock {
    //主键ID、书籍编号、库存、是否上架
    private Integer id;
    private String bookNo;
    private Integer stock;
    private Byte isActive;
}
