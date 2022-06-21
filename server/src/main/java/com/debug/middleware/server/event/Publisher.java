package com.debug.middleware.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件描述
 * Spring事件驱动模型，生产者
 * @author dingguo.an
 * @date 2022年05月27日 19:32
 */
@Component
public class Publisher {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(Publisher.class);
    //定义发送消息的组件
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 发送消息的方法
     * @throws Exception
     */
    public void sendMsg() throws Exception{
        //构造用户登录成功后的用户实体信息
        LoginEvent event = new LoginEvent(this, "debug", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "127.0.0.1");
        //发送消息
        publisher.publishEvent(event);
        log.info("Spring事件驱动模型-发送消息：{}", event);
    }
}
