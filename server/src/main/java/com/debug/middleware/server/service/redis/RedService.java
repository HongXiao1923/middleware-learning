package com.debug.middleware.server.service.redis;

import com.debug.middleware.model.entity.RedDetail;
import com.debug.middleware.model.entity.RedRecord;
import com.debug.middleware.model.entity.RedRobRecord;
import com.debug.middleware.model.mapper.RedDetailMapper;
import com.debug.middleware.model.mapper.RedRecordMapper;
import com.debug.middleware.model.mapper.RedRobRecordMapper;
import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 文件描述
 * 红包业务逻辑处理工程数据记录接口实现类，异步实现
 * @author dingguo.an
 * @date 2022年04月23日 15:52
 */
@Service
@EnableAsync
public class RedService implements IRedService {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RedService.class);

    //注入属性：发红包标识串处理、发红包随机金额处理、抢红包的数据处理
    @Autowired(required = false)
    private RedRecordMapper redRecordMapper;
    @Autowired(required = false)
    private RedDetailMapper redDetailMapper;
    @Autowired(required = false)
    private RedRobRecordMapper redRobRecordMapper;

    /**
     * 发红包记录，异步方式
     * @param dto 红包总金额 +个数
     * @param redId 红包全局唯一标识
     * @param list 红包随机金额列表
     * @throws Exception
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
        //创建实体类对象，并设置字段值
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());
        //将对象的信息存入数据库
        redRecordMapper.insertSelective(redRecord);
        //创建红包金额明细实体类对象，并将随机金额列表的信息存入相应的字段
        RedDetail detail;
        for(Integer i : list){
            detail = new RedDetail();
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            detail.setCreateTime(new Date());
            //将对象存入数据库
            redDetailMapper.insertSelective(detail);
        }
    }

    /**
     * 抢红包记录，异步实现
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    @Override
    @Async
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());

        //将实体对象写入数据库中
        redRobRecordMapper.insertSelective(redRobRecord);
    }
}
