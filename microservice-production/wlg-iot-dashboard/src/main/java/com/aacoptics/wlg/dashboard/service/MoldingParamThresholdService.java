package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.MoldingMachine;
import com.aacoptics.wlg.dashboard.entity.MoldingParamThreshold;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MoldingParamThresholdService {
    IPage<MoldingParamThreshold> query(Page page, Integer machineId);

    boolean add(MoldingParamThreshold moldingParamThreshold);

    boolean delete(Integer id);

    boolean update(MoldingParamThreshold moldingParamThreshold);
}