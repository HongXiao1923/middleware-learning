package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.Item;
import org.apache.ibatis.annotations.Param;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月20日 11:09
 */
public interface ItemMapper {
    //添加商品信息的增删改查方法
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Item record);

    int updateByPrimaryKeySelective(Item record);

    //根据商品编号查询商品信息
    Item selectByCode(@Param("code") String code);
}
