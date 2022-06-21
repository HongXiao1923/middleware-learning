package com.debug.middleware.server.service.redis;

import com.debug.middleware.model.entity.Item;
import com.debug.middleware.model.mapper.ItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月21日 12:30
 */
@Service
public class CachePassService {
    private static final Logger log = LoggerFactory.getLogger(CachePassService.class);
    //定义Mapper
    @Autowired
    private ItemMapper itemMapper;
    //定义Redis操作组件
    @Autowired
    private RedisTemplate redisTemplate;
    //定义JSON序列化与反序列化框架
    @Autowired
    private ObjectMapper objectMapper;
    //定义缓存中Key命名前缀
    private static final String keyPrefix = "item";

    /**
     * 获取商品详情
     * 如果缓存有，则直接从缓存中取；
     * 如果没有，则从数据库中查询，并将查询结果存入缓存中
     */
    public Item getItemInfo(String itemCOde) throws Exception{
        //定义商品对象
        Item item = null;
        //定义缓存中真正的Key，由前缀和商品编码组成
        final String key = keyPrefix + itemCOde;
        //定义Redis组件
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(redisTemplate.hasKey(key)){
            log.info("---获取商品详情-缓存中存在该商品---商品编号：{}",itemCOde);
            //从缓存中查询该商品
            Object res = valueOperations.get(key);
            if(res != null && !Strings.isNullOrEmpty(res.toString())){
                //如果可以找到该商品，则进行JSON反序列化解析
                item = objectMapper.readValue(res.toString(), Item.class);
            }
        }else{
            log.info("---获取商品详情-缓存中不存在该商品---从数据库中查询---商品编号为：{}",itemCOde);
            //从数据库中获取该商品详情
            item = itemMapper.selectByCode(itemCOde);
            if(item != null){
                //从数据库中查询到了该商品，将其序列化并写入缓存
                valueOperations.set(key, objectMapper.writeValueAsString(item));
            }else{
                //过期时间设置为30分钟
                valueOperations.set(key,"",30L, TimeUnit.MINUTES);
            }
        }
        return item;
    }
}
