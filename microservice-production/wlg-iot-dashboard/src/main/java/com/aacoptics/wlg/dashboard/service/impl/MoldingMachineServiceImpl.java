package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineMapper;
import com.aacoptics.wlg.dashboard.service.MoldingMachineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class MoldingMachineServiceImpl extends ServiceImpl<MoldingMachineMapper, MoldingMachine> implements MoldingMachineService {

    @Override
    public IPage<MoldingMachine> query(Page page, String machineName) {
        QueryWrapper<MoldingMachine> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(machineName), "machine_name", machineName);
        queryWrapper.orderByAsc("machine_name");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean update(MoldingMachine moldingMachine) {
        UpdateWrapper<MoldingMachine> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("updated_time", LocalDateTime.now())
                .set("updated_by", UserContextHolder.getInstance().getUsername())
                .set("feeding_alarm", moldingMachine.isFeedingAlarm())
                .set("standard_ct", moldingMachine.getStandardCt())
                .eq("id", moldingMachine.getId());

        return this.update(updateWrapper);
    }
}