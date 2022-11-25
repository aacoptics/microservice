package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.param.CycleDetailParam;
import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.File;
import java.io.IOException;

public interface CycleDetailService {
    boolean update(CycleDetail cycleDetail);
    IPage<CycleDetail> query(Page page, CycleDetailParam cycleDetailParam);
    File exportExcel(CycleDetailParam cycleDetailParam) throws IOException;
}