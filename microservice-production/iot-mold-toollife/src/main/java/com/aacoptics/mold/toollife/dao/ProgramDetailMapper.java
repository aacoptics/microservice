package com.aacoptics.mold.toollife.dao;

import com.aacoptics.mold.toollife.entity.ProgramDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProgramDetailMapper extends BaseMapper<ProgramDetail> {

    List<ProgramDetail> getLastDayOee(@Param("startTime") String startTime);

    ProgramDetail getAbnormalTotalTime(@Param("toolCode") String toolCode);

    List<ProgramDetail> getToolHisList(@Param("toolCode") String toolCode);

    String getLastDayOEEByType(@Param("startTime") String startTime, @Param("type") String type);
}
