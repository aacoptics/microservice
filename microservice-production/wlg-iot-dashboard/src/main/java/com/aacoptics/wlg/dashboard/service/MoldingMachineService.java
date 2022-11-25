package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MoldingMachineService {

    IPage<MoldingMachine> query(Page page, String machineName);

    boolean update(MoldingMachine moldingMachine);
}
