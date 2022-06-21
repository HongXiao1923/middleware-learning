package com.debug.middleware.server;

import com.debug.middleware.server.event.Publisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年05月27日 19:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EventTest {
    @Autowired
    private Publisher publisher;
    @Test
    public void test() throws Exception{
        //调用生产者发送消息
        publisher.sendMsg();
    }
}
