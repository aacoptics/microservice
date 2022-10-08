package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionOrderItemMapper;
import com.aacoptics.wlg.equipment.service.InspectionOrderItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InspectionOrderItemServiceImpl extends ServiceImpl<InspectionOrderItemMapper, InspectionOrderItem> implements InspectionOrderItemService {

    @Resource
    InspectionOrderItemMapper inspectionOrderItemMapper;


    @Override
    public boolean add(InspectionOrderItem inspectionOrderItem) {


        boolean isSuccess = this.save(inspectionOrderItem);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(InspectionOrderItem inspectionOrderItem) {

        boolean isSuccess = this.updateById(inspectionOrderItem);
        return isSuccess;
    }


    @Override
    public InspectionOrderItem get(Long id) {
        InspectionOrderItem inspectionOrderItem = this.getById(id);
        if (Objects.isNull(inspectionOrderItem)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return inspectionOrderItem;
    }

    @Override
    public boolean saveBatch(List<InspectionOrderItem> inspectionOrderItemList) {
        return this.saveBatch(inspectionOrderItemList);
    }
}
