package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucDataEntity;
import com.aacoptics.fanuc.dashboard.entity.FanucDigitalData;
import com.aacoptics.fanuc.dashboard.entity.RealFanucStatusInfo;

import java.util.List;
import java.util.Map;

public interface FanucDashboardService {

    FanucDataEntity getDetailInfo(String machineName);

    List<RealFanucStatusInfo> getByFloor();

    List<FanucDigitalData> getDigitalData();

    Map<String, Map<String, Integer>> getStatusCount();

    Map<String, String> getCurrentOeeData();

}
