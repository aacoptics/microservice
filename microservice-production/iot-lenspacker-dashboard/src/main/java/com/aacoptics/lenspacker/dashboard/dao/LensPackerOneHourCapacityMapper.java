package com.aacoptics.lenspacker.dashboard.dao;

import com.aacoptics.lenspacker.dashboard.entity.LensPackerOneHourCapacity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LensPackerOneHourCapacityMapper extends BaseMapper<LensPackerOneHourCapacity> {
    List<LensPackerOneHourCapacity> getTotalUph(@Param("startTime")  String startTime);

    List<LensPackerOneHourCapacity> getMachineCapacity(@Param("startTime") String startTime, @Param("endTime") String endTime);
}