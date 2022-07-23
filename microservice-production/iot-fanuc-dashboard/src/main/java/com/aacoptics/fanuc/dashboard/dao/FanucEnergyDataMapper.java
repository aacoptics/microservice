package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.FanucEnergyData;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FanucEnergyDataMapper extends BaseMapper<FanucEnergyData> {

    @DS("fanucDB")
    List<FanucEnergyData> getLastMouthEnergy();

    @DS("fanucDB")
    List<FanucEnergyData> getCurrentEnergy();

}