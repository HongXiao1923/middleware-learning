package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.UserAccount;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/7/8 17:04
 */

public interface UserAccountMapper {
    /**
     * 乐观锁
     */

    UserAccount selectByPrimaryKey(Integer id);

    UserAccount selectByUserId(@Param("userId") Integer userId);

    int updateAmount(@Param("money") Double money, @Param("id") Integer id);

    int updateByPKVersion(@Param("money") Double money, @Param("id") Integer id, @Param("version") Integer version);

    /**
     * 悲观锁
     */

    UserAccount selectByUserIdLock(@Param("userId") Integer userId);

    int updateAmountLock(@Param("money") Double money, @Param("id") Integer id);
}
