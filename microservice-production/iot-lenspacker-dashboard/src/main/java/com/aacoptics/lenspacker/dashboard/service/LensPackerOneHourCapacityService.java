package com.aacoptics.lenspacker.dashboard.service;

import com.aacoptics.lenspacker.dashboard.entity.LensPackerOneHourCapacity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface LensPackerOneHourCapacityService extends IService<LensPackerOneHourCapacity> {

    List<LensPackerOneHourCapacity> getTotalUph();

    List<LensPackerOneHourCapacity> getMachineCapacity(String startTime, String endTime);
}
