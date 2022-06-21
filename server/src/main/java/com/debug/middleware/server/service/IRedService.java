package com.debug.middleware.server.service;

import com.debug.middleware.server.dto.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文件描述
 * 红包业务逻辑处理工程数据记录接口，异步实现
 * @author dingguo.an
 * @date 2022年04月23日 15:41
 */
public interface IRedService {
    /**
     * 记录发红包时的全局唯一标识串、随机金额、红包个数等存入数据库
     * @param dto
     * @param redId
     * @param list
     * @throws Exception
     */
    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;

    /**
     * 记录抢红包时用户抢到的金额等信息存入数据库
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;

}
