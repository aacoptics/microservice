package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.MoldingMachineParamData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingParamAnalysisData;
import com.alibaba.fastjson.JSONArray;

import java.time.LocalDateTime;
import java.util.List;

public interface MoldingMachineParamDataService {

    List<MoldingMachineParamData> getMoldingParamData(String machineName,
                                                      String paramName,
                                                      List<String> waferIds);

    JSONArray getMoldingParamDataArray(String machineName,
                                       String paramName,
                                       List<String> waferIds);

    List<MoldingMachineParamData> getWaferIds(String machineName,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime);

    List<MoldingMachineParamData> getMachineName();

    List<MoldingMachineParamData> getMoldingParamName(String machineName,
                                                      List<String> waferIds);

    List<MoldingParamAnalysisData> getAnalysisData(String machineName,
                                                   List<String> paramNames,
                                                   LocalDateTime startTime,
                                                   LocalDateTime endTime);
}
