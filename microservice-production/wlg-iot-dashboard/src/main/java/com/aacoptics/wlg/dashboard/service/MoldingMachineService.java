package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.MoldingMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface MoldingMachineService {

    IPage<MoldingMachine> query(Page page, String machineName);

}
