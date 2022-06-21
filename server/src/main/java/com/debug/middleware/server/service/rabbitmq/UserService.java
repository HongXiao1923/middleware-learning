package com.debug.middleware.server.service.rabbitmq;

import com.debug.middleware.model.entity.User;
import com.debug.middleware.model.mapper.UserMapper;
import com.debug.middleware.server.dto.UserLoginDto;
import com.debug.middleware.server.rabbitmq.publisher.LoginPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Arthur
 * @version 1.0
 * @description: 用户服务类
 * @date 2022/6/16 15:30
 */
@Service
public class UserService {
    //日志
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    //注入用户实体对应Mapper接口
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private LoginPublisher loginPublisher;

    /**
     * 用户登录服务
     * @param dto
     * @return
     * @throws Exception
     */
    public Boolean login(UserLoginDto dto) throws Exception{
        //根据用户名和密码查询用户实体记录
        User user = userMapper.selectByUserNamePassword(dto.getUserName(), dto.getPassword());
        //表示数据表中存在该用户，且密码是匹配的
        if(user != null){
            //表明用户已经登录成功，需要赋值相关信息
            dto.setUserId(user.getId());
            //发送登录成功日志信息
            loginPublisher.sendLogMsg(dto);
            //返回登录成功
            return true;
        }else{
            return false;
        }
    }
}
