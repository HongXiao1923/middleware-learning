package com.debug.middleware.server.controller.lock;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.UserRegDto;
import com.debug.middleware.server.service.lock.UserRegService;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arthur
 * @version 1.0
 * @description: 定义处理用户注册请求的服务实例
 * @date 2022/7/21 22:13
 */
@RestController
public class UserRegController {
    //日志、请求前缀
    private static final Logger log = LoggerFactory.getLogger(UserRegController.class);
    private static final String prefix = "user/reg";
    @Autowired
    private UserRegService userRegService;

    /**
     * 提交用户注册信息
     * @param dto
     * @return
     */
    @RequestMapping(value = prefix + "/submit", method = RequestMethod.GET)
    public BaseResponse reg(UserRegDto dto){
        //检验用户名、密码等信息
        if(Strings.isNullOrEmpty(dto.getUserName()) || Strings.isNullOrEmpty(dto.getPassword())){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //定义返回实例信息
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //处理用户提交的请求，不加分布式锁
            //userRegService.userRegNoLock(dto);
            //处理用户提交的请求，加上 Redis 分布式锁
            //userRegService.userRegWithLock(dto);
            //处理用户提交的请求，加上 ZooKeeper 分布式锁
            userRegService.userRegWithZLock(dto);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        //
        return response;
    }
}
