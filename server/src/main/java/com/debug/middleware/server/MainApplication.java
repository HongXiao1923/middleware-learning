package com.debug.middleware.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
//import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月14日 14:32
 */
@SpringBootApplication
@MapperScan(basePackages = "com.debug.middleware.model")
public class MainApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    //读取环境变量实例
    @Autowired
    private Environment env;
    //自定义注入Bean-ZooKeeper高度封装过的服务端CuratorFramework实例
    @Bean
    public CuratorFramework curatorFramework(){
        //创建CuratorFramework实例
        //采用工厂模式创建
        //指定了客户端连接到ZooKeeper服务端的策略，这里采用重试的机制（5次，每次间隔1秒）
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(env.getProperty("zk.host"))
                .namespace(env.getProperty("zk.namespace")).retryPolicy(new RetryNTimes(5, 1000)).build();
        curatorFramework.start();

        return curatorFramework;
    }

}
