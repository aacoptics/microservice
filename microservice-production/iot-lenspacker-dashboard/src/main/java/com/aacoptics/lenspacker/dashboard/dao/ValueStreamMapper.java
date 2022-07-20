package com.aacoptics.lenspacker.dashboard.dao;

import com.aacoptics.lenspacker.dashboard.entity.ValueStream;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ValueStreamMapper extends BaseMapper<ValueStream> {
    List<ValueStream> getMachineAlarmDetail(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<ValueStream> getMachineAlarmCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<ValueStream> getMachineCapacity(@Param("startTime") String startTime, @Param("endTime") String endTime);
}