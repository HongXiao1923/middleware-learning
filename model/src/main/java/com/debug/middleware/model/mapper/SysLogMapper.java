package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.SysLog;
import com.debug.middleware.model.entity.User;

/**
 * @author Arthur
 * @version 1.0
 * @description: 登录用户日志Mapper接口
 * @date 2022/6/15 16:27
 */
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysLog record);

    int updateByPrimaryKeySelective(SysLog record);
}
