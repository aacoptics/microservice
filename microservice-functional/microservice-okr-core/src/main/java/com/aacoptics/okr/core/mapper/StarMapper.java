package com.aacoptics.okr.core.mapper;

import com.aacoptics.okr.core.entity.po.Star;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StarMapper extends BaseMapper<Star> {
}
