package com.debug.middleware.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Arthur
 * @version 1.0
 * @description: 操作日志实体信息
 * @date 2022/6/14 17:34
 */
@Data
public class SysLog {
    //主键ID、用户ID、用户操作所属模块、操作数据、备注、操作时间
    private Integer id;
    private Integer userId;
    private String module;
    private String data;
    private String memo;
    private Date createTime;
}
