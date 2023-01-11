package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucDataEntity;
import com.aacoptics.fanuc.dashboard.entity.FanucDigitalData;
import com.aacoptics.fanuc.dashboard.entity.FanucMonitData;
import com.aacoptics.fanuc.dashboard.entity.RealFanucStatusInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FanucDashboardService {

    FanucDataEntity getDetailInfo(String machineName);

    List<RealFanucStatusInfo> getByFloor();

    List<FanucDigitalData> getDigitalData();

    Map<String, Map<String, Integer>> getStatusCount();

    Map<String, String> getCurrentOeeData();

    /**
     * 获取注塑机统计数据
     * @param machineName
     * @param paramNames
     * @param startTime
     * @param endTime
     * @return
     */
    List<FanucMonitData> getAnalysisData(String machineName,
                                         List<String> paramNames,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime);

}
