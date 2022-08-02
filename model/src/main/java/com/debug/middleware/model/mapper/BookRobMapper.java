package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.BookRob;
import com.debug.middleware.model.entity.BookStock;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: 书籍抢购实体操作接口
 * @date 2022/7/27 18:40
 */
public interface BookRobMapper {

    int insertSelective(BookRob record);

    int countByBookNoUserId(@Param("userId") Integer userId, @Param("bookNo") String bookNo);
}
