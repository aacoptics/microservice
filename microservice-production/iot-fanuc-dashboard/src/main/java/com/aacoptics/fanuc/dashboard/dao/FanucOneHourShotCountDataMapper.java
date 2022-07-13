package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.FanucOneHourShotCountData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FanucOneHourShotCountDataMapper extends BaseMapper<FanucOneHourShotCountData> {

    List<FanucOneHourShotCountData> getUPH(@Param("startTime")  String startTime);

    List<FanucOneHourShotCountData> getTotalUph(@Param("startTime")  String startTime);
}