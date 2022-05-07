package com.aacoptics.organization.mapper;

import com.aacoptics.organization.entity.po.DictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 10:16
 */
@Repository
@Mapper
public interface DictTypeMapper extends BaseMapper<DictType> {
}