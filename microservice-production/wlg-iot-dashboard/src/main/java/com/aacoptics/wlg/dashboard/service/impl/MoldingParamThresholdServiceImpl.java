package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.MoldingMachine;
import com.aacoptics.wlg.dashboard.entity.MoldingParamThreshold;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingParamThresholdMapper;
import com.aacoptics.wlg.dashboard.service.MoldingMachineService;
import com.aacoptics.wlg.dashboard.service.MoldingParamThresholdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class MoldingParamThresholdServiceImpl extends ServiceImpl<MoldingParamThresholdMapper, MoldingParamThreshold> implements MoldingParamThresholdService {

    @Override
    public IPage<MoldingParamThreshold> query(Page page, Integer machineId) {
        QueryWrapper<MoldingParamThreshold> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("machine_id", machineId);
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(MoldingParamThreshold moldingParamThreshold) {
        return this.save(moldingParamThreshold);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MoldingParamThreshold moldingParamThreshold) {
        return this.updateById(moldingParamThreshold);
    }
}