package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.InspectionOrderStatusConstants;
import com.aacoptics.wlg.equipment.constant.RepairOrderSourceConstants;
import com.aacoptics.wlg.equipment.constant.RepairOrderStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.RepairOrderMapper;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.RepairOrderService;
import com.aacoptics.wlg.equipment.service.SequenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairOrderService {

    @Resource
    RepairOrderMapper repairOrderMapper;

    @Resource
    EquipmentService equipmentService;

    @Resource
    SequenceService sequenceService;

    @Override
    public IPage<RepairOrderVO> query(Page page, RepairOrderQueryParam repairOrderQueryParam) {

        IPage<RepairOrderVO> repairOrderVOIPage = repairOrderMapper.findRepairOrderList(page, repairOrderQueryParam);

        return repairOrderVOIPage;
    }


    @Override
    @Transactional
    public boolean add(RepairOrder repairOrder) {
        String mchCode = repairOrder.getMchCode();
        //校验设备编码是否存在
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if (equipment == null) {
            throw new BusinessException("设备编码【" + mchCode + "】不存在，请确认");
        }
        repairOrder.setDutyPersonId(equipment.getDutyPersonId());

        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);
        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_MANUAL);

        //生成工单号
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = orderNumberDateFormatter.format(currentDate);

        repairOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));

        boolean isSuccess = this.save(repairOrder);
        if (isSuccess == false) {
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


    @Override
    public Long getOrCreateSequenceNumber(String sequenceName) {
        Long nextSequenceNumber = sequenceService.getNextNumberByName(sequenceName);
        if (nextSequenceNumber == null) {
            Sequence sequence = sequenceService.createSequence(sequenceName, "维修工单流水号", 1l, 1l, 999999l);
            nextSequenceNumber = sequence.getSequenceNumber();
        }
        return nextSequenceNumber;
    }

    @Override
    public String getNextOrderNumber(String currentDate) {
        String sequenceName = "REPAIR_ORDER_" + currentDate;

        Long nextSequenceNumber = this.getOrCreateSequenceNumber(sequenceName);

        String nextSequenceNumberStr = currentDate + String.format("%04d", nextSequenceNumber);

        return nextSequenceNumberStr;
    }


    @Override
    public void batchConfirm(List<String> repairOrderIds) {
        if (repairOrderIds == null || repairOrderIds.size() == 0) {
            throw new BusinessException("请至少选择一个维修工单");
        }
        for (int i = 0; i < repairOrderIds.size(); i++) {
            String idStr = repairOrderIds.get(i);
            RepairOrder repairOrder = this.getById(Long.valueOf(idStr));
            if (!RepairOrderStatusConstants.REPAIRED.equals(repairOrder.getStatus())) {
                throw new BusinessException("工单【" + repairOrder.getOrderNumber() + "】不是已维修状态，不能确认");
            }
            repairOrder.setStatus(RepairOrderStatusConstants.CONFIRMED);
            this.update(repairOrder);
        }
    }


    @Override
    public RepairOrderVO findOrderByMchCode(String mchCode) {
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if (equipment == null) {
            throw new BusinessException("设备【" + mchCode + "】不存在，请确认！");
        }

        RepairOrderVO repairOrderVO = repairOrderMapper.findOrderByMchCode(mchCode);
        if (repairOrderVO == null) {
            throw new BusinessException("设备【" + mchCode + "】不存在需要维修的工单，请确认！");
        }

        return repairOrderVO;
    }

    @Override
    public boolean submitOrder(RepairOrder repairOrder) {

        repairOrder.setRepairDatetime(LocalDateTime.now());
        boolean isSuccess = this.updateById(repairOrder);
        return isSuccess;
    }

    @Override
    public RepairOrder createRepairOrderByInspection(InspectionOrder inspectionOrder, InspectionOrderItem inspectionOrderItem) {

        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setMchCode(inspectionOrder.getMchCode());
        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_INSPECTION);
        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);

        repairOrder.setSourceOrderId(inspectionOrder.getId());
        repairOrder.setSourceOrderItemId(inspectionOrderItem.getId());
        repairOrder.setFaultDesc(inspectionOrderItem.getFaultDesc());
        repairOrder.setFaultImageId(inspectionOrderItem.getFaultImageId());
        repairOrder.setDutyPersonId(inspectionOrder.getDutyPersonId());

        this.save(repairOrder);

        return repairOrder;
    }

    @Override
    public RepairOrder createRepairOrderByMaintenance(MaintenanceOrder maintenanceOrder, MaintenanceOrderItem maintenanceOrderItem) {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setMchCode(maintenanceOrder.getMchCode());
        repairOrder.setDutyPersonId(maintenanceOrder.getDutyPersonId());

        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_MAINTENANCE);
        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);

        repairOrder.setSourceOrderId(maintenanceOrder.getId());
        repairOrder.setSourceOrderItemId(maintenanceOrderItem.getId());
        repairOrder.setFaultDesc(maintenanceOrderItem.getFaultDesc());
        repairOrder.setFaultImageId(maintenanceOrderItem.getFaultImageId());

        this.save(repairOrder);

        return repairOrder;
    }
}
