package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.wlg.equipment.constant.*;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.RepairOrderMapper;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.MessageService;
import com.aacoptics.wlg.equipment.service.RepairOrderService;
import com.aacoptics.wlg.equipment.service.SequenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    @Resource
    MessageService messageService;

    @Override
    public IPage<RepairOrderVO> query(Page page, RepairOrderQueryParam repairOrderQueryParam) {

        IPage<RepairOrderVO> repairOrderVOIPage = repairOrderMapper.findRepairOrderList(page, repairOrderQueryParam);

        return repairOrderVOIPage;
    }


    @Override
    public List<RepairOrderVO> queryRepairOrderByCondition(RepairOrderQueryParam repairOrderQueryParam) {

        List<RepairOrderVO> repairOrderVOIPage = repairOrderMapper.findRepairOrderList(repairOrderQueryParam);

        return repairOrderVOIPage;
    }


    @Override
    @Transactional
    public boolean add(RepairOrder repairOrder) {
        String mchCode = repairOrder.getMchCode();
        //??????????????????????????????
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if (equipment == null) {
            throw new BusinessException("???????????????" + mchCode + "????????????????????????");
        }
        //??????????????????????????????????????????????????????
        repairOrder.setDutyPersonId(equipment.getEquipDuty() != null ? equipment.getEquipDuty() : equipment.getDutyPersonId());

        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);
        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_MANUAL);

        //???????????????
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = orderNumberDateFormatter.format(currentDate);

        repairOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));

        boolean isSuccess = this.save(repairOrder);
        if (isSuccess == false) {
            throw new BusinessException("????????????????????????");
        }
        //?????????????????????????????????
        messageService.sendEquipmentAllRepairMessage(repairOrder);

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
            throw new BusinessException("ID???" + id + "?????????????????????");
        }

        return repairOrder;
    }


    @Override
    public Long getOrCreateSequenceNumber(String sequenceName) {
        Long nextSequenceNumber = sequenceService.getNextNumberByName(sequenceName);
        if (nextSequenceNumber == null) {
            Sequence sequence = sequenceService.createSequence(sequenceName, "?????????????????????", 1l, 1l, 999999l);
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
            throw new BusinessException("?????????????????????????????????");
        }
        for (int i = 0; i < repairOrderIds.size(); i++) {
            String idStr = repairOrderIds.get(i);
            RepairOrder repairOrder = this.getById(Long.valueOf(idStr));
            if (!RepairOrderStatusConstants.REPAIRED.equals(repairOrder.getStatus())) {
                throw new BusinessException("?????????" + repairOrder.getOrderNumber() + "???????????????????????????????????????");
            }

            //???????????????????????????
            Equipment equipment = equipmentService.findEquipmentByMchCode(repairOrder.getMchCode());
            equipment.setStatus(EquipmentStatusConstants.NORMAL);
            equipmentService.update(equipment);


            repairOrder.setStatus(RepairOrderStatusConstants.CONFIRMED);
            this.update(repairOrder);

        }
    }


    @Override
    public List<RepairOrderVO> findOrderByMchCode(String mchCode) {
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if (equipment == null) {
            throw new BusinessException("?????????" + mchCode + "???????????????????????????");
        }
        HashMap<String, String> conditionMap = new HashMap<>();
        conditionMap.put("mchCode", mchCode);
        List<RepairOrderVO> repairOrderVOList = repairOrderMapper.findOrderByCondition(conditionMap);
        if (repairOrderVOList == null || repairOrderVOList.size() == 0) {
            throw new BusinessException("?????????" + mchCode + "????????????????????????????????????????????????");
        }

        return repairOrderVOList;
    }

    @Override
    public List<RepairOrderVO> findOrderByUser(String user) {
        HashMap<String, String> conditionMap = new HashMap<>();
        if(StringUtils.isEmpty(user))
        {
            conditionMap.put("user", UserContextHolder.getInstance().getUsername());
        }
        else
        {
            conditionMap.put("user", user);
        }
        List<RepairOrderVO> repairOrderVOList = repairOrderMapper.findOrderByCondition(conditionMap);
        return repairOrderVOList;
    }


    @Override
    public boolean submitOrder(RepairOrder repairOrder) {
        String orderStatus = repairOrder.getStatus();
        if(!RepairOrderStatusConstants.STAGED.equals(orderStatus) && !RepairOrderStatusConstants.REPAIRED.equals(orderStatus))
        {
            throw new BusinessException("???????????????1(?????????)???2(?????????)????????????");
        }
        RepairOrder targetRepairOrder = this.get(repairOrder.getId());
        if(targetRepairOrder == null)
        {
            throw new BusinessException("ID??????" + repairOrder.getId() + "?????????????????????????????????");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        targetRepairOrder.setRepairDesc(repairOrder.getRepairDesc());
        targetRepairOrder.setRepairDatetime(currentTime);
        //??????????????????
        if(targetRepairOrder.getStageDatetime() == null) {
            targetRepairOrder.setStageDatetime(currentTime);
        }
        targetRepairOrder.setHandleMethod(repairOrder.getHandleMethod());
        targetRepairOrder.setReason(repairOrder.getReason());
        targetRepairOrder.setIsClosed(repairOrder.getIsClosed());
        targetRepairOrder.setLongTermMeasure(repairOrder.getLongTermMeasure());

        boolean isSuccess = this.updateById(targetRepairOrder);

        return isSuccess;
    }

    @Override
    public RepairOrder createRepairOrderByInspection(InspectionOrder inspectionOrder, InspectionOrderItem inspectionOrderItem) {
        RepairOrder repairOrder = new RepairOrder();

        //???????????????
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = orderNumberDateFormatter.format(currentDate);

        repairOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));

        repairOrder.setMchCode(inspectionOrder.getMchCode());
        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_INSPECTION);
        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);

        repairOrder.setSourceOrderId(inspectionOrder.getId());
        repairOrder.setSourceOrderItemId(inspectionOrderItem.getId());
        repairOrder.setFaultDesc(inspectionOrderItem.getFaultDesc());
        repairOrder.setFaultImageId(inspectionOrderItem.getFaultImageId());
        repairOrder.setDutyPersonId(inspectionOrder.getDutyPersonId());

        this.save(repairOrder);

        //?????????????????????????????????
        messageService.sendEquipmentAllRepairMessage(repairOrder);

        return repairOrder;
    }

    @Override
    public RepairOrder createRepairOrderByMaintenance(MaintenanceOrder maintenanceOrder, MaintenanceOrderItem maintenanceOrderItem) {
        RepairOrder repairOrder = new RepairOrder();
        //???????????????
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = orderNumberDateFormatter.format(currentDate);

        repairOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));

        repairOrder.setMchCode(maintenanceOrder.getMchCode());
        repairOrder.setDutyPersonId(maintenanceOrder.getDutyPersonId());

        repairOrder.setSourceType(RepairOrderSourceConstants.ORDER_SOURCE_TYPE_MAINTENANCE);
        repairOrder.setStatus(RepairOrderStatusConstants.CREATED);

        repairOrder.setSourceOrderId(maintenanceOrder.getId());
        repairOrder.setSourceOrderItemId(maintenanceOrderItem.getId());
        repairOrder.setFaultDesc(maintenanceOrderItem.getFaultDesc());
        repairOrder.setFaultImageId(maintenanceOrderItem.getFaultImageId());

        this.save(repairOrder);

        //?????????????????????????????????
        messageService.sendEquipmentAllRepairMessage(repairOrder);

        return repairOrder;
    }
}
