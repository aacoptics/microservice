package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.MoldingAbnormalData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingEventData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;

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

    IPage<MoldingEventData> getMachineStatus(String machineName,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             Page page);
}
