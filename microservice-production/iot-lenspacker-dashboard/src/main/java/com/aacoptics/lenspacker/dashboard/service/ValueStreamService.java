package com.aacoptics.lenspacker.dashboard.service;

import com.aacoptics.lenspacker.dashboard.entity.ValueStream;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ValueStreamService extends IService<ValueStream> {
    List<ValueStream> getMachineAlarmDetail(String startTime, String endTime);

    List<ValueStream> getMachineAlarmCount(String startTime, String endTime);

    List<ValueStream> getMachineCapacity(String startTime, String endTime);
}
