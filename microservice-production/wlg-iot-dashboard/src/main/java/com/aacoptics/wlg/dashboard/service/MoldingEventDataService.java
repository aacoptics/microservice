package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.alibaba.fastjson.JSONArray;

import java.time.LocalDateTime;
import java.util.List;

public interface MoldingEventDataService {
    List<MoldingEventData> getMachineEvents(String machineName,
                                            LocalDateTime startTime,
                                            LocalDateTime endTime);
}
