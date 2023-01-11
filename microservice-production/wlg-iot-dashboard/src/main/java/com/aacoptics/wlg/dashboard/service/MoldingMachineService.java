package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.aacoptics.wlg.dashboard.entity.vo.MoldingMachineStatusSummaryVO;
import com.aacoptics.wlg.dashboard.entity.vo.MoldingMachineStatusVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MoldingMachineService {

    IPage<MoldingMachine> query(Page page, String machineName);

    boolean update(MoldingMachine moldingMachine);

    /**
     * 获取机台当前状态信息
     *
     * @param machineName
     * @return
     */
    MoldingMachineStatusVO getMachineStatusInfo(String machineName);

    /**
     * 获取机台状态汇总信息
     *
     * @return
     */
    MoldingMachineStatusSummaryVO getMachineStatusSummaryInfo();
}
