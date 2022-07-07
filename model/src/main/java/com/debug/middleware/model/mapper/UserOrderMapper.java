package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.UserOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/6/27 21:59
 */
public interface UserOrderMapper {

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrder record);

    //根据下单记录ID和支付状态查询
    UserOrder selectByIdAndStatus(@Param("id") Integer id, @Param("status")Integer status);
}
