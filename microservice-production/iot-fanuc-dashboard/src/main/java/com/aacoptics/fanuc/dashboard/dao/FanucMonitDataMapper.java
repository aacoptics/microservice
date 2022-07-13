package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.FanucMonitData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface FanucMonitDataMapper extends BaseMapper<FanucMonitData> {

    List<FanucMonitData> getAllCycleList(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}