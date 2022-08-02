package com.debug.middleware.server.service.lock;

import com.debug.middleware.model.entity.UserAccount;
import com.debug.middleware.model.entity.UserAccountRecord;
import com.debug.middleware.model.mapper.UserAccountMapper;
import com.debug.middleware.model.mapper.UserAccountRecordMapper;
import com.debug.middleware.server.dto.UserAccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 基于数据库的乐观锁、悲观锁的服务类
 * @date 2022/7/11 21:21
 */
@Service
public class DataBaseLockService {
    private static final Logger log = LoggerFactory.getLogger(DataBaseLockService.class);
    @Autowired(required = false)
    private UserAccountMapper userAccountMapper;
    @Autowired(required = false)
    private UserAccountRecordMapper userAccountRecordMapper;

    /**
     * 用户账户提取金额处理
     * @param dto
     * @throws Exception
     */
    public void takeMoney(UserAccountDto dto) throws Exception{
        //查询用户账户实体记录
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());
        //判断实体记录是否存在以及账户余额及账户余额是否能够被提现
        if(userAccount != null && userAccount.getAmount().doubleValue() - dto.getAmount() > 0){
            //如果能够被提现，则更新现有账户余额
            userAccountMapper.updateAmount(dto.getAmount(), userAccount.getId());
            //同时记录提现成功时的记录
            UserAccountRecord record = new UserAccountRecord();
            //设置提现成功时的时间
            record.setCreateTime(new Date());
            //设置账户记录主键ID
            record.setAccountId(userAccount.getId());
            //设置成功提现时的金额
            record.setMoney(BigDecimal.valueOf(dto.getAmount()));
            //插入申请提现的金额到历史记录
            userAccountRecordMapper.insert(record);
            //输出日志
            log.info("当前待提现的金额为：{}，用户账户余额为：{}", dto.getAmount(), userAccount.getAmount());
        }else{
            throw new Exception("账户不存在或者账户余额不足！");
        }
    }

    /**
     * 用户账户提取金额处理（乐观锁处理方式）
     * @param dto
     * @throws Exception
     */
    public void takeMoneyWithLock(UserAccountDto dto) throws Exception{
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());
        if(userAccount != null && userAccount.getAmount().doubleValue() - dto.getAmount() > 0){
            //如果可以提现，则更新现有账户余额（采用“版本号version”机制）
            int res = userAccountMapper.updateByPKVersion(dto.getAmount(), userAccount.getId(), userAccount.getVersion());
            //只有更新成功时（此时 res = 1, 即数据库执行更新语句之后数据库受影响的记录行数
            if(res > 0){
                //同时记录提现成功时的记录
                UserAccountRecord record = new UserAccountRecord();
                record.setCreateTime(new Date());
                record.setAccountId(userAccount.getId());
                record.setMoney(BigDecimal.valueOf(dto.getAmount()));
                userAccountRecordMapper.insert(record);
                log.info("当前呆提现的金额为：{}，用户账户余额为：{}", dto.getAmount(), userAccount.getAmount());
            }
        }else{
            throw new Exception("账户不存在或者账户余额不足！");
        }
    }

    public void takeMoneyWithLockNegtive(UserAccountDto dto) throws Exception{
        //查询用户账户实体记录，采用 for update 方式
        UserAccount userAccount = userAccountMapper.selectByUserIdLock(dto.getUserId());
        //判断实体记录是否存在以及账户余额是否足够被提现
        if(userAccount != null && userAccount.getAmount().doubleValue() - dto.getAmount() > 0){
            //满足提现条件，采用“版本号 version” 机制更新余额
            int res = userAccountMapper.updateAmountLock(dto.getAmount(), userAccount.getId());
            //更新成功时，返回受影响的行数
            if(res > 0){
                //同时记录提现成功时的记录
                UserAccountRecord record = new UserAccountRecord();
                record.setCreateTime(new Date());
                record.setAccountId(userAccount.getId());
                record.setMoney(BigDecimal.valueOf(dto.getAmount()));
                //记入数据库并输出日志
                userAccountRecordMapper.insert(record);
                log.info("悲观锁的处理方式-当前待提现的金额为：{}，用户账户余额为：{}", dto.getAmount(), userAccount.getAmount());
            }
        }else{
            log.info("悲观锁的处理方式-账户不存在或者账户余额不足");
        }
    }

}
