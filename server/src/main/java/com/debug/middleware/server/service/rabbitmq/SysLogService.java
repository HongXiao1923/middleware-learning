package com.debug.middleware.server.service.rabbitmq;

import com.debug.middleware.model.entity.SysLog;
import com.debug.middleware.model.mapper.SysLogMapper;
import com.debug.middleware.server.dto.UserLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 系统日志服务
 * @date 2022/6/16 16:08
 */
@Service
public class SysLogService {
    //日志
    private static final Logger log = LoggerFactory.getLogger(SysLogService.class);
    //引入相应的组件
    @Autowired(required = false)
    private SysLogMapper sysLogMapper;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 用户登录成功的信息写入数据库
     * @param dto
     */
    @Async
    public void recordLog(UserLoginDto dto){
        try{
            //定义系统日志对象，并设置相应参数的取值
            SysLog entity = new SysLog();
            entity.setUserId(dto.getUserId());
            entity.setModule("用户登录模块");
            entity.setData(objectMapper.writeValueAsString(dto));
            entity.setMemo("用户登录成功记录相关登录信息");
            entity.setCreateTime(new Date());

            //写入数据库
            sysLogMapper.insertSelective(entity);
        }catch (Exception e){
            log.error("系统日志服务-记录用户登录成功的信息入数据库-发生异常：{}", dto, e.fillInStackTrace());
        }
    }
}
