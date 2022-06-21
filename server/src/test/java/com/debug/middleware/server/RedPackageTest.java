package com.debug.middleware.server;

import com.debug.middleware.server.utils.RedPackageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月23日 11:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedPackageTest {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedPackageTest.class);

    //二倍均值法自测
    @Test
    public void one() throws Exception{
        //以10元（单位为分），10人为例
        Integer amount = 1000;
        Integer total = 10;
        //调用二倍均值法得到小红包列表
        List<Integer> list = RedPackageUtil.divideRedPackage(amount, total);
        log.info("总金额={}分，总个数={}个", amount, total);
        //验证算法是否合理
        Integer sum = 0;
        for(Integer i : list){
            log.info("随机金额为：{}分，即{}元", i, new BigDecimal(i.toString()).divide(new BigDecimal(100)));
            sum += i;
        }
        log.info("所有的随机金额叠加之和为{}", sum);
    }

}
