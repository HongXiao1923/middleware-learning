package com.debug.middleware.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * 文件描述
 * Spring事件驱动模型，消费者
 * @author dingguo.an
 * @date 2022年05月27日 19:26
 */
//加入Spring的IOC容器、允许异步执行
@Component
@EnableAsync
public class Consumer implements ApplicationListener<LoginEvent> {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    /**
     * 监听消费消息
     * @param loginEvent
     */
    @Override
    @Async
    public void onApplicationEvent(LoginEvent loginEvent) {
        //输出日志信息
        log.info("Spring事件驱动模型-接受消息：{}", loginEvent);

        //TODO：后续为实现自身的业务逻辑，如写入数据库等
    }
}
