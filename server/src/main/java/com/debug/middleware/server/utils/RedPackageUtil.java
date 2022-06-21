package com.debug.middleware.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 文件描述
 * 二倍均值法用于分配红包
 * @author dingguo.an
 * @date 2022年04月23日 11:00
 */
public class RedPackageUtil {
    /**
     * 发红包算法，金额参数单位为分
     * @param totalAmount
     * @param totalPeopleNum
     * @return
     */
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum){
        //用于存储每次产生的小红包随机金额列表，金额单位为分
        List<Integer> amountList = new ArrayList<>();
        //判断总金额和总人数参数的合法性
        if(totalAmount > 0 && totalPeopleNum > 0){
            //记录剩余的总金额，初始化时即为红包的总金额
            Integer resAmount = totalAmount;
            //记录剩余的总人数，初始化时即为指定的总人数
            Integer resPeopleNum = totalPeopleNum;
            //定义产生随机数的实例对象
            Random random = new Random();
            //迭代（发红包的过程）
            for(int i = 0; i < totalPeopleNum - 1; i++){
                //随机区间，左闭右开，amount即为产生的随机金额
                int amount = random.nextInt(resAmount / resPeopleNum * 2 - 1) + 1;
                //更新数据
                resAmount -= amount;
                resPeopleNum--;
                amountList.add(amount);
            }
            //循环完毕，添加最后一个随机金额到列表中
            amountList.add(resAmount);
        }
        return amountList;
    }
 }
