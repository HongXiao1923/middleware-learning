package com.debug.middleware.server.controller.rabbitmq;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.UserLoginDto;
import com.debug.middleware.server.service.rabbitmq.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户登录controller
 * @date 2022/6/15 19:17
 */
@RestController
public class UserController {
    //日志
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    //前段请求前缀
    private static final String prefix = "user";
    //注入
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = prefix+"/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse login(@RequestBody @Validated UserLoginDto dto, BindingResult result){
        //检验前段用户提交的用户登录数据信息的合法性
        if(result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //定义返回结果实例
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //调用Service层的方法处理用户登录逻辑
            Boolean res = userService.login(dto);
            if(res){
                //res = true，表示用户登录成功
                response = new BaseResponse(StatusCode.Success.getCode(), "登录成功");
            }else{
                //登录失败
                response = new BaseResponse(StatusCode.Fail.getCode(), "登录失败-账号密码不匹配");
            }
        }catch (Exception e){
            //表示处理过程发生异常
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        //返回最终结果
        return response;
    }
}
