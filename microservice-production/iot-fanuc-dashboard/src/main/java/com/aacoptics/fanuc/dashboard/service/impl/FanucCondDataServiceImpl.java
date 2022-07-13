package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucCondDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucCondData;
import com.aacoptics.fanuc.dashboard.service.FanucCondDataService;
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
public class FanucCondDataServiceImpl extends ServiceImpl<FanucCondDataMapper, FanucCondData> implements FanucCondDataService {

    @Autowired
    FanucCondDataMapper fanucCondDataMapper;

    @Override
    public List<FanucCondData> getCondDataByTime(String startTime, String endTime, String machineName) {
        QueryWrapper<FanucCondData> wrapper = new QueryWrapper<>();
        wrapper.between("dbCreateTime",startTime, endTime)
        .eq("monit_mc_name", machineName);
        return fanucCondDataMapper.selectList(wrapper);
    }
}
