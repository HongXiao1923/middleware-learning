package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.MqOrder;

/**
 * @author Arthur
 * @version 1.0
 * @description: 失效订单的状态实体对应的Mapper操作接口
 * @date 2022/6/27 21:41
 */
public interface MqOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MqOrder record);

    int insertSelective(MqOrder record);

    MqOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(MqOrder record);

    int updateByPrimaryKeySelective(MqOrder record);
}
