package com.aacoptics.data.analysis.mapper;

import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MoldFlowMapper extends BaseMapper<MoldFlowData> {
}
