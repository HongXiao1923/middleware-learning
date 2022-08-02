package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.UserAccountRecord;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/7/8 17:26
 */
public interface UserAccountRecordMapper {

    int insert(UserAccountRecord record);

    UserAccountRecord selectByPrimaryKey(Integer id);
}
