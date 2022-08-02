package com.debug.middleware.server.controller.lock;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.BookRobDto;
import com.debug.middleware.server.service.lock.BookRobService;
import net.bytebuddy.agent.builder.LambdaFactory;
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
 * @description: 书籍抢购请求处理
 * @date 2022/7/27 20:21
 */
@RestController
public class BookRobController {
    //日志、请求前缀
    private static final Logger log = LoggerFactory.getLogger(BookRobController.class);
    private static final String prefix = "book/rob";
    @Autowired
    private BookRobService bookRobService;

    /**
     * 用户书籍抢购请求
     * @param dto
     * @return
     */
    @RequestMapping(value = prefix + "/request", method = RequestMethod.GET)
    public BaseResponse takeBook(BookRobDto dto){
        //校验参数
        if(Strings.isNullOrEmpty(dto.getBookNo()) || dto.getUserId() == null || dto.getUserId() <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //不加锁的情况
            //bookRobService.robWithNoLock(dto);
            //加锁的情况
            bookRobService.robWithZLock(dto);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
