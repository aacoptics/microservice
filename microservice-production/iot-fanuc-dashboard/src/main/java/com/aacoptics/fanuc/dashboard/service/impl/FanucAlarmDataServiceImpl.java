package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucAlarmDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucAlarmData;
import com.aacoptics.fanuc.dashboard.service.FanucAlarmDataService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@DS("fanucDB")
public class FanucAlarmDataServiceImpl extends ServiceImpl<FanucAlarmDataMapper, FanucAlarmData> implements FanucAlarmDataService {

    @Autowired
    FanucAlarmDataMapper fanucAlarmDataMapper;

    @Override
    public List<FanucAlarmData> getAlarmDataByTime(String startTime, String endTime, String machineName) {
        QueryWrapper<FanucAlarmData> wrapper = new QueryWrapper<>();
        wrapper.between("convert(datetime, alarm_date)",startTime, endTime)
        .eq("monit_mc_name", machineName);
        return fanucAlarmDataMapper.selectList(wrapper);
    }
}
