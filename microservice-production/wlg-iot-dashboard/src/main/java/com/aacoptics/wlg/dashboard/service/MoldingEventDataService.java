package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.MoldingAbnormalData;
import com.aacoptics.wlg.dashboard.entity.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface MoldingEventDataService {
    IPage<MoldingEventData> getMachineEvents(String machineName,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             Page page);

    IPage<MoldingEventData> getMachineErrors(String machineName,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             Page page);

    IPage<MoldingAbnormalData> getMachineAbnormalData(String machineName,
                                                LocalDateTime startTime,
                                                LocalDateTime endTime,
                                                Page page);
}
