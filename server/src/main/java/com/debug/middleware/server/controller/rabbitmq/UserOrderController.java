package com.debug.middleware.server.controller.rabbitmq;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.UserOrderDto;
import com.debug.middleware.server.service.redis.DeadUserOrderService;
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
 * @description: 用户下单记录
 * @date 2022/7/1 16:08
 */
@RestController
public class UserOrderController {
    //日志
    private static final Logger log = LoggerFactory.getLogger(UserOrderController.class);
    //定义请求前缀
    private static final String prefix = "user/order";
    //用户下单处理服务实例
    @Autowired
    private DeadUserOrderService deadUserOrderService;

    /**
     * 用户下单请求的接受与处理
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = prefix + "/push", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse login(@RequestBody @Validated UserOrderDto dto, BindingResult result){
        //判断请求的参数是否合法
        if(result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //定义响应结果实例
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //调用Service层真正处理用户下单的业务逻辑
            deadUserOrderService.pushUserOrder(dto);
        }catch (Exception e){
            //若处理过程发生异常，抛出并返给前端
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
