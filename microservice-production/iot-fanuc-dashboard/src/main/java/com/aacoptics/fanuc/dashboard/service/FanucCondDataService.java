package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucCondData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FanucCondDataService extends IService<FanucCondData> {
    List<FanucCondData> getCondDataByTime(String startTime, String endTime, String machineName);
}
