package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: 实体类的Mapper操作接口
 * @date 2022/6/15 15:52
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(User record);

    int updateByPrimaryKeySelective(User record);
    //根据用户名和密码查询
    User selectByUserNamePassword(@Param("userName") String userName, @Param("password") String password);
}
