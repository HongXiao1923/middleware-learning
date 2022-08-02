package com.debug.middleware.server.controller.lock;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.UserAccountDto;
import com.debug.middleware.server.service.lock.DataBaseLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arthur
 * @version 1.0
 * @description: 基于数据库的乐观锁、悲观锁
 * @date 2022/7/11 21:17
 */
@RestController
public class DataBaseLockController {
    //日志、请求前缀
    private static final Logger log = LoggerFactory.getLogger(DataBaseLockController.class);
    private static final String prefix = "db";
    @Autowired
    private DataBaseLockService dataBaseLockService;

    /**
     * 用户账户余额提现申请（乐观锁）
     * @param dto
     * @return
     */
    @RequestMapping(value = prefix + "/money/take", method = RequestMethod.GET)
    public BaseResponse takeMoney(UserAccountDto dto){
        //判断参数合法性
        if(dto.getAmount() == null || dto.getUserId() == null){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //定义响应接口实例
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //开始调用核心业务逻辑处理方法，不加锁
            //dataBaseLockService.takeMoney(dto);
            //开始调用核心业务逻辑处理方法，乐观锁
            //dataBaseLockService.takeMoneyWithLock(dto);
            //开始调用核心业务逻辑处理方法，悲观锁
            dataBaseLockService.takeMoneyWithLockNegtive(dto);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

}
