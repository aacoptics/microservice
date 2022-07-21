package com.aacoptics.general.dashboard.service;

import com.aacoptics.general.dashboard.entity.LensPackerAlarmInfo;

import java.util.List;
import java.util.Map;

public interface GeneralDashboardService {

    Map<String, Integer> getGeneralStatusCount();

    List<LensPackerAlarmInfo> getGeneralCurrentAlarm();
}
