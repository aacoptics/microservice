package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.ItemTypeConstants;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MaintenanceItemMapper;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class MaintenanceItemServiceImpl extends ServiceImpl<MaintenanceItemMapper, MaintenanceItem> implements MaintenanceItemService {

    @Resource
    MaintenanceItemMapper maintenanceItemMapper;


    @Override
    public boolean add(MaintenanceItem maintenanceItem) {
        //校验是否存在
        QueryWrapper<MaintenanceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("maintenance_item", maintenanceItem.getMaintenanceItem());
        queryWrapper.eq("maintenance_main_id", maintenanceItem.getMaintenanceMainId());

        Integer selectCount = maintenanceItemMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }
        if(ItemTypeConstants.RANGE.equals(maintenanceItem.getItemType())) {
            if (maintenanceItem.getMinValue().compareTo(maintenanceItem.getMaxValue()) > 0) {
                throw new BusinessException("起始范围值必须小于或等于截至值，请确认！");
            }
            maintenanceItem.setTheoreticalValue(null);
        }
        else
        {
            if(StringUtils.isEmpty(maintenanceItem.getTheoreticalValue()))
            {
                throw new BusinessException("理论值不能为空！");
            }
            maintenanceItem.setMinValue(null);
            maintenanceItem.setMaxValue(null);
        }

        boolean isSuccess = this.save(maintenanceItem);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MaintenanceItem maintenanceItem) {
        //校验是否存在
        QueryWrapper<MaintenanceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("maintenance_item", maintenanceItem.getMaintenanceItem());
        queryWrapper.eq("maintenance_main_id", maintenanceItem.getMaintenanceMainId());
        queryWrapper.ne("id", maintenanceItem.getId());

        Integer selectCount = maintenanceItemMapper.selectCount(queryWrapper);
        if(selectCount > 0)
        {
            throw new BusinessException("相同记录已存在，请确认！");
        }

        if(ItemTypeConstants.RANGE.equals(maintenanceItem.getItemType())) {
            if (maintenanceItem.getMinValue().compareTo(maintenanceItem.getMaxValue()) > 0) {
                throw new BusinessException("起始范围值必须小于或等于截至值，请确认！");
            }
            maintenanceItem.setTheoreticalValue(null);
        }
        else
        {
            if(StringUtils.isEmpty(maintenanceItem.getTheoreticalValue()))
            {
                throw new BusinessException("理论值不能为空！");
            }
            maintenanceItem.setMinValue(null);
            maintenanceItem.setMaxValue(null);
        }
        boolean isSuccess = this.updateById(maintenanceItem);
        return isSuccess;
    }


    @Override
    public MaintenanceItem get(Long id) {
        MaintenanceItem maintenanceItem = this.getById(id);
        if (Objects.isNull(maintenanceItem)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return maintenanceItem;
    }


}
