package com.debug.middleware.server.controller.redis;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 文件描述
 * 红包处理逻辑
 * @author dingguo.an
 * @date 2022年04月23日 16:31
 */
@RestController
public class RedPacketController {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RedPacketController.class);
    //路径前缀
    private static final String prefix = "red/packet";
    //注入属性
    @Autowired
    private IRedPacketService redPacketService;

    /**
     * 处理发红包请求，请求方法为POST，请求参数用JSON格式提交
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = prefix + "/hand/out", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse handout(@Validated @RequestBody RedPacketDto dto, BindingResult result){
        //参数校验
        if(result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //核心业务逻辑处理服务，最终返回红包全局唯一标识串
            String redId = redPacketService.handout(dto);
            response.setData(redId);
        }catch (Exception e){
            log.error("发红包发生异常：dto={}", dto, e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    /**
     * 处理抢红包请求，接受当前用户id和红包的全局唯一标识串参数
     * @param userId
     * @param redId
     * @return
     */
    @RequestMapping(value = prefix + "/rob", method = RequestMethod.GET)
    public BaseResponse rob(@RequestParam Integer userId, @RequestParam String redId){
        //定义响应对象
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            BigDecimal result = redPacketService.rob(userId, redId);
            if(result != null){
                //将抢到的金额返回给前端
                response.setData(result);
            }else{
                response = new BaseResponse(StatusCode.Fail.getCode(), "红包已抢完");
            }
        }catch (Exception e){
            //处理过程发生异常，则输出异常信息并返回给前端
            log.error("抢红包发生异常：userId={}, redId={}", userId, redId, e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        //返回处理结果给前端
        return response;
    }
}
