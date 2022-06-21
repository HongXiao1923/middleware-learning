package com.debug.middleware.server.event;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * 文件描述
 * 用户登录成功后的事件实体
 * @author dingguo.an
 * @date 2022年05月27日 19:22
 */
@Data
@ToString
public class LoginEvent extends ApplicationEvent implements Serializable {
    //用户名、登录时间、所在IP
    private String userName;
    private String loginTime;
    private String ip;
    //构造方法1
    public LoginEvent(Object source) {
        super(source);
    }
    //构造方法2：重载
    public LoginEvent(Object source, String userName, String loginTime, String ip) {
        super(source);
        this.userName = userName;
        this.loginTime = loginTime;
        this.ip = ip;
    }
}
