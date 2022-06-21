package com.debug.middleware.server.service;

import com.debug.middleware.server.dto.RedPacketDto;

import java.math.BigDecimal;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月23日 15:50
 */
public interface IRedPacketService {
    /**
     * 发红包核心业务类实现
     * @param dto
     * @return
     * @throws Exception
     */
    String handout(RedPacketDto dto) throws Exception;

    /**
     * 抢红包
     * @param userId
     * @param redId
     * @return
     * @throws Exception
     */
    BigDecimal rob(Integer userId, String redId) throws Exception;
}
