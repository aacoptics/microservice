package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.aacoptics.okr.core.mapper.PeriodInfoMapper;
import com.aacoptics.okr.core.service.PeriodInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PeriodInfoServiceImpl extends ServiceImpl<PeriodInfoMapper, PeriodInfo> implements PeriodInfoService {
    @Override
    public boolean add(PeriodInfo periodInfo) {
        return this.save(periodInfo);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(PeriodInfo periodInfo) {
        return this.updateById(periodInfo);
    }

    @Override
    public List<PeriodInfo> listAll() {
        QueryWrapper<PeriodInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("period_status", 0)
                .orderByDesc("start_time")
                .orderByDesc("period_status");
        return this.list(queryWrapper);
    }

    @Override
    public PeriodInfo getById(Long id) {
        QueryWrapper<PeriodInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("period_status", 0)
                .eq("id", id);
        return this.getOne(queryWrapper);
    }
}