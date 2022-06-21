package com.debug.middleware.server.controller.redis;

import com.debug.middleware.server.service.redis.CachePassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件描述
 * 用于接收前端用户“获取热销商品信息”的请求
 * 处理缓存穿透
 * @author dingguo.an
 * @date 2022年04月21日 11:26
 */
@RestController
public class CachePassController {
    private static final Logger log = LoggerFactory.getLogger(CachePassController.class);
    private static final String prefix = "cache/pass";
    //定义缓存穿透服务类
    @Autowired
    private CachePassService cachePassService;
    /**
     * 获取热销商品信息
     */
    @RequestMapping(value = prefix+"/item/info", method = RequestMethod.GET)
    public Map<String, Object> getItem(@RequestParam String itemCode){
        //定义接口返回的格式，主要包括code、msg、data
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code",0);
        resMap.put("msg","成功");
        try{
            //调用缓存穿透处理服务类得到返回结果，并将其添加进结果Map中
            resMap.put("data", cachePassService.getItemInfo(itemCode));
        }catch (Exception e){
            resMap.put("code", -1);
            resMap.put("msg","失败");
        }
        return resMap;
    }

}
