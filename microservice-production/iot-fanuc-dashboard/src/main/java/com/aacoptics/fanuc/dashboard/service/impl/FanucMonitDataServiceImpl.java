package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucMonitDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucMonitData;
import com.aacoptics.fanuc.dashboard.service.FanucMonitDataService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@DS("fanucDB")
public class FanucMonitDataServiceImpl extends ServiceImpl<FanucMonitDataMapper, FanucMonitData> implements FanucMonitDataService {

    @Autowired
    FanucMonitDataMapper fanucMonitDataMapper;

    @Override
    public List<FanucMonitData> getMonitDataByTime(String startTime, String endTime, String machineName) {
        QueryWrapper<FanucMonitData> wrapper = new QueryWrapper<>();
        wrapper.between("dbCreateTime", startTime, endTime)
                .eq("monit_mc_name", machineName);
        return fanucMonitDataMapper.selectList(wrapper);
    }

    @Override
    public List<FanucMonitData> getAllCycleList(LocalDateTime startTime, LocalDateTime endTime) {
        return fanucMonitDataMapper.getAllCycleList(startTime, endTime);
    }
}