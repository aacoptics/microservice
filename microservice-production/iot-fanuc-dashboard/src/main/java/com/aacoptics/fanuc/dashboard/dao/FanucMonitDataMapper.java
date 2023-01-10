package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.FanucMonitData;
import com.baomidou.dynamic.datasource.annotation.DS;
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


    @DS("fanucDB")
    List<FanucMonitData> getAnalysisData(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("machineName") String machineName,
                                                   @Param("paramNames")  List<String> paramNames);
}