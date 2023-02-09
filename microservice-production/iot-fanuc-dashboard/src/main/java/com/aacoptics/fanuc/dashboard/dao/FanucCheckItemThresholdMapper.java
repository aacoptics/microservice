package com.aacoptics.fanuc.dashboard.dao;

import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@DS("fanucDB")
public interface FanucCheckItemThresholdMapper extends BaseMapper<FanucCheckItemThreshold> {
}