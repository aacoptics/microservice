package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MaintenanceOrderItemMapper;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MaintenanceOrderItemServiceImpl extends ServiceImpl<MaintenanceOrderItemMapper, MaintenanceOrderItem> implements MaintenanceOrderItemService {

    @Resource
    MaintenanceOrderItemMapper maintenanceOrderItemMapper;


    @Override
    public boolean add(MaintenanceOrderItem maintenanceOrderItem) {


        boolean isSuccess = this.save(maintenanceOrderItem);
        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MaintenanceOrderItem maintenanceOrderItem) {

        boolean isSuccess = this.updateById(maintenanceOrderItem);
        return isSuccess;
    }


    @Override
    public MaintenanceOrderItem get(Long id) {
        MaintenanceOrderItem maintenanceOrderItem = this.getById(id);
        if (Objects.isNull(maintenanceOrderItem)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }
        return maintenanceOrderItem;
    }

    @Override
    public boolean saveBatch(List<MaintenanceOrderItem> maintenanceOrderItemList) {
        return this.saveBatch(maintenanceOrderItemList);
    }
}
