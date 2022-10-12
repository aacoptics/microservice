package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.ShapingResultData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ShapingResultDataMapper extends BaseMapper<ShapingResultData> {
}
