package com.debug.middleware.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
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

}
