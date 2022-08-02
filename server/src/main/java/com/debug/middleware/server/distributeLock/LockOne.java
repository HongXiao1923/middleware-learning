package com.debug.middleware.server.distributeLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arthur
 * @version 1.0
 * @description: 锁机制——模拟ATM机存取钱机制
 * @date 2022/7/7 16:27
 */
public class LockOne {
    private static final Logger log = LoggerFactory.getLogger(LockOne.class);

    public static void main(String[] args) {
        //存钱实例
        Thread tAdd = new Thread(new LockThread(500));
        //取钱实例
        Thread tSub = new Thread(new LockThread(-500));
        //存钱线程开启
        tAdd.start();
        //取钱线程开启
        tSub.start();
    }
}
