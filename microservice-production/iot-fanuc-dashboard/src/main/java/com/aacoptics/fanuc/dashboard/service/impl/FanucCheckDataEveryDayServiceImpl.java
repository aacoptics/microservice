package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucCheckDataEveryDayMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucAlarmData;
import com.aacoptics.fanuc.dashboard.entity.FanucCheckDataEveryDay;
import com.aacoptics.fanuc.dashboard.service.FanucCheckDataEveryDayService;
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
public class FanucCheckDataEveryDayServiceImpl extends ServiceImpl<FanucCheckDataEveryDayMapper, FanucCheckDataEveryDay> implements FanucCheckDataEveryDayService {

    @Autowired
    FanucCheckDataEveryDayMapper fanucCheckDataEveryDayMapper;

    @Override
    public List<String> getAllMachineName() {
        return fanucCheckDataEveryDayMapper.getAllMachineName();
    }

    @Override
    public List<String> getAllMoldFileName() {
        return fanucCheckDataEveryDayMapper.getAllMoldFileName();
    }

    @Override
    public List<FanucCheckDataEveryDay> getFanucCheckDataEveryDay(List<String> machineNameList, List<String> moldFileNameList, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<FanucCheckDataEveryDay> wrapper = new QueryWrapper<>();
        wrapper.between("db_create_time",startTime, endTime);
        if(machineNameList != null && machineNameList.size() > 0)
        {
            wrapper.in("machine_name", machineNameList);
        }
        if(moldFileNameList != null && moldFileNameList.size() > 0)
        {
            wrapper.in("mold_file_name", moldFileNameList);
        }
        wrapper.orderByAsc("machine_name");
        wrapper.orderByAsc("mold_file_name");
        wrapper.orderByAsc("db_create_time");
        return fanucCheckDataEveryDayMapper.selectList(wrapper);
    }
}
