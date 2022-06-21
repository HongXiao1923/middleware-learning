package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.RedRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月21日 22:08
 */
public interface RedRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RedRecord record);

    int insertSelective(RedRecord record);

    RedRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RedRecord record);

    int updateByPrimaryKeySelective(RedRecord record);
}
