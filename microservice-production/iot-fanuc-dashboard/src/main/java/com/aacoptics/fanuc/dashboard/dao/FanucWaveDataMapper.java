package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FanucWaveDataMapper extends BaseMapper<FanucWaveData> {

    @DS("waveDB")
    List<FanucWaveData> getFanucWaveData(@Param("cycleNos") List<Integer> cycleNos);
}