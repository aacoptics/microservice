package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.AbnormalType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AbnormalTypeMapper extends BaseMapper<AbnormalType> {

}
