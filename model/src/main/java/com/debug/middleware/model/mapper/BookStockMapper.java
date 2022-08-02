package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.BookStock;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: 书籍库存实体操作接口
 * @date 2022/7/27 18:40
 */
public interface BookStockMapper {

    BookStock selectByBookNo(@Param("bookNo") String bookNo);

    //更新库存，不加锁
    int updateStock(@Param("bookNo") String bookNo);

    //更新库存，加锁
    int updateStockWithLock(@Param("bookNo") String bookNo);
}
