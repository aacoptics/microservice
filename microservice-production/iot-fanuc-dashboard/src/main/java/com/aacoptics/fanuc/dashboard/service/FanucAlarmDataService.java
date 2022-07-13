package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucAlarmData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FanucAlarmDataService extends IService<FanucAlarmData> {
    List<FanucAlarmData> getAlarmDataByTime(String startTime, String endTime, String machineName);
}
