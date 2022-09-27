package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.RepairOrderMapper;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.RepairOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairOrderService {

    @Resource
    RepairOrderMapper repairOrderMapper;

    @Resource
    EquipmentService equipmentService;


    @Override
    public IPage<RepairOrderVO> query(Page page, RepairOrderQueryParam repairOrderQueryParam) {

        IPage<RepairOrderVO> repairOrderVOIPage = repairOrderMapper.findRepairOrderList(page, repairOrderQueryParam);

        return repairOrderVOIPage;
    }


    @Override
    public boolean add(RepairOrder repairOrder) {
        boolean isSuccess = this.save(repairOrder);
        if(isSuccess == false)
        {
            throw new BusinessException("新增维修工单失败");
        }

        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(RepairOrder repairOrder) {

        boolean isSuccess = this.updateById(repairOrder);
        return isSuccess;
    }


    @Override
    public RepairOrder get(Long id) {
        RepairOrder repairOrder = this.getById(id);
        if (Objects.isNull(repairOrder)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return repairOrder;
    }


    @Transactional
    @Override
    public void generateRepairOrder(RepairOrder repairOrder) {
        log.info("开始生成设备维修工单");
        log.info("完成生成设备维修工单");
    }
}
