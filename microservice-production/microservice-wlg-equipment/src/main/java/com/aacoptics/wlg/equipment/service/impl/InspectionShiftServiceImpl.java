package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionShiftMapper;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class InspectionShiftServiceImpl extends ServiceImpl<InspectionShiftMapper, InspectionShift> implements InspectionShiftService {

    @Resource
    InspectionShiftMapper inspectionShiftMapper;


    @Override
    public boolean add(InspectionShift inspectionShift) {
        //校验是否存在
        QueryWrapper<InspectionShift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shift", inspectionShift.getShift());
        queryWrapper.eq("inspection_main_id", inspectionShift.getInspectionMainId());

        Integer selectCount = inspectionShiftMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }


        boolean isSuccess = this.save(inspectionShift);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(InspectionShift inspectionShift) {
        //校验是否存在
        QueryWrapper<InspectionShift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shift", inspectionShift.getShift());
        queryWrapper.eq("inspection_main_id", inspectionShift.getInspectionMainId());
        queryWrapper.ne("id", inspectionShift.getId());
        Integer selectCount = inspectionShiftMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }
        boolean isSuccess = this.updateById(inspectionShift);
        return isSuccess;
    }


    @Override
    public InspectionShift get(Long id) {
        InspectionShift inspectionShift = this.getById(id);
        if (Objects.isNull(inspectionShift)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return inspectionShift;
    }


}
