package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucMonitData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface FanucMonitDataService extends IService<FanucMonitData> {
    List<FanucMonitData> getMonitDataByTime(String startTime, String endTime, String machineName);

    List<FanucMonitData> getAllCycleList(LocalDateTime startTime, LocalDateTime endTime);
}
