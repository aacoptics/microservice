package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.ToolInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@Mapper
public interface ToolInfoMapper extends BaseMapper<ToolInfo> {

    List<ToolInfo> getToolMaintainStatus(@Param("monitorNos") Set<String> monitorNos);

    Integer getActualLifeByToolCode(@Param("matToolCode") String matToolCode);

    List<Map<String, Object>> getAbnormalToolLifeRatio();

    List<Map<String, Object>> getAbnormalQty();
}
