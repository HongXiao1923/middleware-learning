package com.debug.middleware.model.mapper;


import com.debug.middleware.model.entity.RedRobRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月21日 22:08
 */
public interface RedRobRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RedRobRecord record);

    int insertSelective(RedRobRecord record);

    RedRobRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RedRobRecord record);

    int updateByPrimaryKeySelective(RedRobRecord record);
}
