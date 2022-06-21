package com.debug.middleware.server.service.redis;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedPacketService;
import com.debug.middleware.server.service.IRedService;
import com.debug.middleware.server.utils.RedPackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月23日 16:10
 */
@Service
public class RedPacketService implements IRedPacketService {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RedPacketService.class);
    //redis缓存的key前缀
    private static final String keyPrefix = "redis:red:packet:";
    //注入属性
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IRedService redService;

    /**
     * 发红包
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public String handout(RedPacketDto dto) throws Exception {
        if(dto.getTotal() > 0 && dto.getAmount() > 0){
            //采用二倍均值法生成随机金额列表
            List<Integer> list = RedPackageUtil.divideRedPackage(dto.getAmount(), dto.getTotal());
            //生成红包全局唯一标识串
            String timestamp = String.valueOf(System.nanoTime());
            String redId = new StringBuffer(keyPrefix).append(dto.getUserId()).append(":").append(timestamp).toString();
            //将随机金额存入缓存列表中
            redisTemplate.opsForList().leftPushAll(redId, list);
            //根据Key的前缀和其他信息拼接成一个新的用于存储红包总数的Key
            String redTotalKey = redId + ":total";
            //将红包总数存入缓存中
            redisTemplate.opsForValue().set(redTotalKey, dto.getTotal());
            //异步记录
            redService.recordRedPacket(dto, redId, list);

            return redId;
        }else{
            throw new Exception("系统异常-分发红包-参数不合法！");
        }
    }

    /**
     * 抢红包（5.13.2022.加入分布式锁实现高并发）
     * @param userId
     * @param redId
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        //定义Redis组件
        ValueOperations valueOperations =  redisTemplate.opsForValue();
        //在抢红包之前需要判断用户是否已经抢过红包了
        //若已抢过，则直接返回红包金额
        Object obj = valueOperations.get(redId + userId + ":rob");
        if(obj != null){
            return new BigDecimal(obj.toString());
        }
        //“点红包”业务逻辑，主要用于判断是否还有红包
        Boolean res = click(redId);
        //拆红包逻辑处理
        if(res) {
            /*
            //从随机金额列表中弹出一个随机金额
            Object value = redisTemplate.opsForList().rightPop(redId);
            //表示抢到红包逻辑
            if(value != null) {
                String redTotalKey = redId + ":total";
                //更新数据信息，红包个数减1
                Integer curTotal = valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
                valueOperations.set(redTotalKey, curTotal - 1);
                //发红包的单位为分，抢红包需要将红包金额的单位设置为元并返给用户，否则在前端也需要进行单位转换
                //除以100即可
                BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                //记录抢到红包时用户的账号信息及金额，信息存入数据库
                redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
                //记录抢到红包的用户信息到Redis，表示已抢过红包
                valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                //输出当前用户抢到红包的记录信息
                log.info("当前用户抢到红包了：userId={} key={} 金额={}", userId, redId, result);

                return result;
            }
            */
            //加分布式锁：一个红包每个人只能抢到一次随机金额，即要永远保证一对一地构造缓存中的Key
            final String localKey = redId + userId + "-lock";
            //调用setIfAbsent()方法，间接实现分布式锁
            Boolean lock = valueOperations.setIfAbsent(localKey, redId);
            //设定该分布式锁的过期时间为24h
            redisTemplate.expire(localKey, 24L, TimeUnit.HOURS);
            try {
                //表示当前线程获得了该分布式锁
                if (lock) {
                    //开始执行抢红包业务逻辑
                    //从随机金额列表中弹出一个随机金额
                    Object value = redisTemplate.opsForList().rightPop(redId);
                    //表示抢到红包逻辑
                    if (value != null) {
                        String redTotalKey = redId + ":total";
                        //更新数据信息，红包个数减1
                        Integer curTotal = valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
                        valueOperations.set(redTotalKey, curTotal - 1);
                        //发红包的单位为分，抢红包需要将红包金额的单位设置为元并返给用户，否则在前端也需要进行单位转换
                        //除以100即可
                        BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                        //记录抢到红包时用户的账号信息及金额，信息存入数据库
                        redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
                        //记录抢到红包的用户信息到Redis，表示已抢过红包
                        valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                        //输出当前用户抢到红包的记录信息
                        log.info("当前用户抢到红包了：userId={} key={} 金额={}", userId, redId, result);

                        return result;
                    }
                }
            } catch (Exception e) {
                throw new Exception("系统异常-抢红包-加分布式锁失败");
            }
        }
        //表示当前用户没有抢到红包
        return null;
    }

    /**
     * 点红包逻辑，判断缓存中是否还有红包
     * @param redId
     * @return
     * @throws Exception
     */
    private Boolean click(String redId) throws Exception{
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //定义用于查询缓存系统中红包剩余个数的Key，并获取Redis缓存中的红包剩余个数
        String redTotalKey = redId +":total";
        Object total = valueOperations.get(redTotalKey);
        //判断红包个数total是否大于0
        if(total != null && Integer.valueOf(total.toString()) > 0){
            return true;
        }
        return false;

    }
}
