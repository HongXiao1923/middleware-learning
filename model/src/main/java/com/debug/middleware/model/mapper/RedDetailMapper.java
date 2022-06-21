package com.debug.middleware.model.mapper;

import com.debug.middleware.model.entity.RedDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月21日 22:03
 */
public interface RedDetailMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RedDetail record);

    int insertSelective(RedDetail record);

    RedDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RedDetail record);

    int updateByPrimaryKeySelective(RedDetail record);
}
