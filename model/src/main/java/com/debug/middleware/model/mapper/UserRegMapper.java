package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.UserReg;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户注册实体信息对应的Mapper接口
 * @date 2022/7/21 21:25
 */
public interface UserRegMapper {

    int insertSelective(UserReg record);

    UserReg selectByUserName(@Param("userName") String userName);
}
