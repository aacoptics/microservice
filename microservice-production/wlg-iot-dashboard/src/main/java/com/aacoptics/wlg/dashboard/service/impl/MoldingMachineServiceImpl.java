package com.aacoptics.wlg.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aacoptics.wlg.dashboard.entity.MoldingMachine;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineMapper;
import com.aacoptics.wlg.dashboard.service.MoldingMachineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MoldingMachineServiceImpl extends ServiceImpl<MoldingMachineMapper, MoldingMachine> implements MoldingMachineService {

    @Override
    public IPage<MoldingMachine> query(Page page, String machineName) {
        QueryWrapper<MoldingMachine> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(machineName), "machine_name", machineName);
        return this.page(page, queryWrapper);
    }
}