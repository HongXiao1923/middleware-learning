package com.debug.middleware.server.distributeLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arthur
 * @version 1.0
 * @description: 模拟锁机制线程类
 * @date 2022/7/7 16:34
 */
public class LockThread implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(LockThread.class);
    //存or取得金额
    private int count;

    public LockThread(int count) {
        this.count = count;
    }

    //未加同步锁
    @Override
    public void run(){
        try{
            //执行10w次访问共享操作
            for(int i = 0; i < 100000; i++){
                //加入Synchronized关键字
                synchronized (SysConstant.amount){
                    //传来金额进行操作（可正可负）
                    SysConstant.amount = SysConstant.amount + count;
                    //输出每次操作玩账户的余额
                    log.info("The current money of the amount -- {}", SysConstant.amount);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
