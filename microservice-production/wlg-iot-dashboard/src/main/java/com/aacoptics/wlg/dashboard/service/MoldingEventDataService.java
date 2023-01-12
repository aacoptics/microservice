package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.MoldingAbnormalData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMK4Data;
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

    IPage<MoldingEventData> getMachineStatus(List<String> machineName,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             Page page);

    IPage<MoldingMK4Data> getMoldingMK4Data(List<String> machineNameList, LocalDateTime startTime, LocalDateTime endTime, Page page);

    IPage<MoldingEventData> getMachineSingleStatus(List<String> machineNameList,
                                                   LocalDateTime startTime,
                                                   LocalDateTime endTime,
                                                   Page page);
}
