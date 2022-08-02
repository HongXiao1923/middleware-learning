package com.debug.middleware.server.service.lock;

import com.debug.middleware.model.entity.BookRob;
import com.debug.middleware.model.entity.BookStock;
import com.debug.middleware.model.mapper.BookRobMapper;
import com.debug.middleware.model.mapper.BookStockMapper;
import com.debug.middleware.server.dto.BookRobDto;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Arthur
 * @version 1.0
 * @description: TODO
 * @date 2022/7/27 19:10
 */
@Service
public class BookRobService {
    private static final Logger log = LoggerFactory.getLogger(BookRobService.class);
    @Autowired(required = false)
    private BookStockMapper bookStockMapper;
    @Autowired(required = false)
    private BookRobMapper bookRobMapper;
    //ZooKeeper客户端
    @Autowired
    private CuratorFramework client;

    /**
     * 处理书籍抢购逻辑，不加分布式锁
     * @param dto
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void robWithNoLock(BookRobDto dto) throws Exception{
        //根据书籍编号查询记录
        BookStock stock = bookStockMapper.selectByBookNo(dto.getBookNo());
        //统计每个用户每本书的抢购数量
        int total = bookRobMapper.countByBookNoUserId(dto.getUserId(), dto.getBookNo());
        //商品记录存在、库存充足，而且用户还没有抢购过这本书，因此可以抢购
        if(stock != null && stock.getStock() > 0 && total <= 0){
            log.info("---处理书籍请购逻辑--不加分布式锁---，当前信息为：{}", dto);
            //当前用户抢到该书，库存减1
            int res = bookStockMapper.updateStock(dto.getBookNo());
            if(res > 0){
                //创建实体并完成信息注册
                BookRob entity = new BookRob();
                BeanUtils.copyProperties(dto, entity);
                entity.setRobTime(new Date());
                bookRobMapper.insertSelective(entity);
            }
        }else{
            throw new Exception("该书籍库存不足！");
        }
    }

    private static final String pathPrefix = "/middleware/zkLock/";
    /**
     * 处理书籍抢购逻辑，加 ZooKeeper 分布式锁
     * @param dto
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void robWithZLock(BookRobDto dto) throws Exception{
        //创建互斥锁实例
        InterProcessMutex mutex = new InterProcessMutex(client, pathPrefix + dto.getBookNo() + dto.getUserId() + " - Lock");
        try{
            //采用互斥组件尝试获取分布式锁，这里的最大尝试时间设置为15s
            if(mutex.acquire(15L, TimeUnit.SECONDS)){
                BookStock stock = bookStockMapper.selectByBookNo(dto.getBookNo());
                //统计每个用户每本书的抢购数量
                int total = bookRobMapper.countByBookNoUserId(dto.getUserId(), dto.getBookNo());
                if(stock != null && stock.getStock() > 0 && total <= 0){
                    log.info("---处理书籍请购逻辑--加ZooKeeper分布式锁---，当前信息为：{}", dto);
                    //当前用户抢购到书籍，库存减1
                    int res = bookStockMapper.updateStock(dto.getBookNo());
                    if(res > 0){
                        BookRob entity = new BookRob();
                        BeanUtils.copyProperties(dto, entity);
                        entity.setRobTime(new Date());
                        bookRobMapper.insertSelective(entity);
                    }
                }else{
                    throw new Exception("该书籍库存不足！");
                }
            }else{
                throw new Exception("获取ZooKeeper锁失败！");
            }
        }catch (Exception e){
            throw e;
        }finally {
            //无论何种情况都需要释放锁
            mutex.release();
        }
    }
}
