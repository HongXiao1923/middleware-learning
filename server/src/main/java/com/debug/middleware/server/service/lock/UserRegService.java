package com.debug.middleware.server.service.lock;

import com.debug.middleware.model.entity.UserReg;
import com.debug.middleware.model.mapper.UserRegMapper;
import com.debug.middleware.server.dto.UserRegDto;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Arthur
 * @version 1.0
 * @description: 处理用户注册信息提交服务
 * @date 2022/7/21 21:47
 */
@Service
public class UserRegService {
    //日志、组件、接口
    private static final Logger log = LoggerFactory.getLogger(UserRegService.class);
    @Autowired(required = false)
    private UserRegMapper userRegMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //定义ZooKeeper客户端CuratorFramework实例
    @Autowired
    private CuratorFramework client;

    /**
     * 处理用户提交注册的请求，不加Redis分布式锁
     * @param dto
     * @throws Exception
     */
    public void userRegNoLock(UserRegDto dto) throws Exception{
        //获取实体
        UserReg reg = userRegMapper.selectByUserName(dto.getUserName());
        //当前用户名还没有被注册，可以注册并写入数据库
        if(reg == null){
            log.info("---不加 Redis 分布式锁---，当前用户名为：{}", dto.getUserName());
            //创建用户实体
            UserReg entity = new UserReg();
            //将前端获取实体的用户的对应字段信息复制到新的实体中
            BeanUtils.copyProperties(dto, entity);
            //设置注册时间并将注册信息加入数据库
            entity.setCreateTime(new Date());
            userRegMapper.insertSelective(entity);
        }else{
            throw new Exception("用户信息已经存在! 注册失败");
        }
    }

    /**
     * 处理用户提交注册的请求，加上Redis分布式锁
     * @param dto
     * @throws Exception
     */
    public void userRegWithLock(UserRegDto dto) throws Exception{
        //精心设计并构造 SETNX 操作的 Key，一定要和实际的业务或共享资源相关联
        final String key = dto.getUserName() + "-lock";
        //设计 Key 对应的 Value，为了具有随机性
        //在这里采用系统提供的纳米级别的时间戳以及 UUID 生成的随机数作为 Value
        final String value = System.nanoTime() + "" + UUID.randomUUID();
        //获取操作 Key 的 ValueOperations 实例
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        //调用 SETNX 命令获取锁，如果返回 true, 表示获取锁成功（当前共享资源还没有被其他线程占用）
        Boolean res = valueOperations.setIfAbsent(key, value);
        if(res){
            //为了防止死锁情况，加上 EXPIRE 操作，设置 Key 的过期时间，在这里设置为 20s（具体视情况而定）
            stringRedisTemplate.expire(key, 20L, TimeUnit.SECONDS);
            try{
                //查询用户实体
                UserReg reg = userRegMapper.selectByUserName(dto.getUserName());
                //当前用户名未注册
                if(reg == null){
                    log.info("---加上 Redis 分布式锁---，当前用户名为：{}", dto.getUserName());
                    UserReg entity = new UserReg();
                    BeanUtils.copyProperties(dto, entity);
                    entity.setCreateTime(new Date());
                    userRegMapper.insertSelective(entity);
                }else{
                    throw new Exception("用户信息已经存在! 注册失败");
                }
            }catch (Exception e){
                throw e;
            }finally {
                //无论成功与否，先前获得的锁都需要释放
                if(value.equals(valueOperations.get(key).toString())){
                    stringRedisTemplate.delete(key);
                }
            }
        }
    }

    //ZooKeeper分布式锁的实现原理是ZNode节点的创建与删除以及监听机制的构成
    //而ZNode节点将对应一个具体的路径，和Unix文件夹路径类似，需要以/开头
    private static final String pathPrefix = "/middleware/zkLock/";
    /**
     * 处理用户提交注册的请求，加ZooKeeper分布式锁
     * @param dto
     * @throws Exception
     */
    public void userRegWithZLock(UserRegDto dto) throws Exception{
        //创建ZooKeeper互斥锁组件实例，需要将监控用的客户端实例、精心构造的共享资源作为构造参数
        InterProcessMutex mutex = new InterProcessMutex(client, pathPrefix + dto.getUserName() + "-lock");
        try{
            //采用互斥锁组件尝试获取分布式锁，其中尝试的最大时间在这里设置为10s（具体根据业务而定）
            if(mutex.acquire(10L, TimeUnit.SECONDS)){
                //根据用户名查询用户实体信息
                UserReg reg = userRegMapper.selectByUserName(dto.getUserName());
                //当前用户名未注册
                if(reg == null){
                    log.info("---加上 ZooKeeper 分布式锁---，当前用户名为：{}", dto.getUserName());
                    //操作实体
                    UserReg entity = new UserReg();
                    BeanUtils.copyProperties(dto, entity);
                    entity.setCreateTime(new Date());
                    userRegMapper.insertSelective(entity);
                }else{
                    throw new Exception("用户信息已经存在！");
                }
            }else{
                throw new Exception("获取 ZooKeeper 分布式锁失败！");
            }
        }catch (Exception e){
            throw e;
        }finally {
            //一定记得释放锁
            mutex.release();
        }
    }
}
