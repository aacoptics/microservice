package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.param.CycleDetailParam;
import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.aacoptics.wlg.dashboard.mapper.CycleDetailMapper;
import com.aacoptics.wlg.dashboard.service.CycleDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CycleDetailServiceImpl extends ServiceImpl<CycleDetailMapper, CycleDetail> implements CycleDetailService {

    @Override
    public boolean update(CycleDetail cycleDetail) {
        return this.updateById(cycleDetail);
    }


    @Override
    public IPage<CycleDetail> query(Page page, CycleDetailParam cycleDetailParam) {
        QueryWrapper<CycleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(null != cycleDetailParam.getMachineNames() && cycleDetailParam.getMachineNames().size() > 0, "machine_name", cycleDetailParam.getMachineNames());
        queryWrapper.ge(null != cycleDetailParam.getStartTime(), "start_time", cycleDetailParam.getStartTime())
                .le(null != cycleDetailParam.getEndTime(), "start_time", cycleDetailParam.getEndTime());
        return this.page(page, queryWrapper);
    }
}