package com.aacoptics.lenspacker.dashboard.service;

import com.aacoptics.lenspacker.dashboard.entity.AlarmData;
import com.aacoptics.lenspacker.dashboard.entity.StatusData;

import java.util.List;
import java.util.Map;

public interface LensPackerDashboardService {

    List<StatusData> getStatusInfo();

    List<AlarmData> getCurrentAlarmInfo();

    Map<String, Map<String, Integer>> getStatusCount();

}
