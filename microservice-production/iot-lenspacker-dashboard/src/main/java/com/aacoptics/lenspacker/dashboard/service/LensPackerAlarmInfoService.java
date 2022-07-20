package com.aacoptics.lenspacker.dashboard.service;

import com.aacoptics.lenspacker.dashboard.entity.LensPackerAlarmInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface LensPackerAlarmInfoService extends IService<LensPackerAlarmInfo> {
    List<LensPackerAlarmInfo> getMachineAlarmDetail(String startTime, String endTime);

    List<LensPackerAlarmInfo> getMachineAlarmCount(String startTime, String endTime);

    List<LensPackerAlarmInfo> getMachineNameList();
}
