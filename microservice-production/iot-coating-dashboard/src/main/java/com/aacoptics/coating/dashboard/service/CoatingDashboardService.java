package com.aacoptics.coating.dashboard.service;

import com.aacoptics.coating.dashboard.entity.CoatingStatusEntities;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface CoatingDashboardService {

    CoatingStatusEntities getCoatingMachineStatus();

    JSONObject getCoatingMachineAlarmInfo();

    Map<String, Map<String, Integer>> getStatusCount();

}
