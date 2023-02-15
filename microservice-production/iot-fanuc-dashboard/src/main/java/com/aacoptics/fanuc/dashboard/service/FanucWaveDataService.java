package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucMasterData;
import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FanucWaveDataService extends IService<FanucWaveData> {
    List<Integer> selectCycleNos(LocalDateTime startTime, LocalDateTime endTime, String machineNo);
    Map<Integer, List<FanucWaveData>> getFanucWaveData(List<Integer> cycleNos);
}
