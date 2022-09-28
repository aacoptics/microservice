package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.InspectionOrderStatusConstants;
import com.aacoptics.wlg.equipment.constant.RepairOrderSourceConstants;
import com.aacoptics.wlg.equipment.constant.RepairOrderStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.entity.po.Sequence;
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
        if(equipment == null)
        {
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



    @Override
    public Long getOrCreateSequenceNumber(String sequenceName) {
        Long nextSequenceNumber =  sequenceService.getNextNumberByName(sequenceName);
        if(nextSequenceNumber == null) {
            Sequence sequence = sequenceService.createSequence(sequenceName, "维修工单流水号", 1l, 1l, 999999l);
            nextSequenceNumber = sequence.getSequenceNumber();
        }
        return nextSequenceNumber;
    }

    @Override
    public String getNextOrderNumber(String currentDate) {
        String sequenceName = "REPAIR_ORDER_" + currentDate;

        Long nextSequenceNumber = this.getOrCreateSequenceNumber(sequenceName);

        String nextSequenceNumberStr =  currentDate + String.format("%04d", nextSequenceNumber);

        return nextSequenceNumberStr;
    }


    @Override
    public void batchConfirm(List<String> repairOrderIds) {
        if(repairOrderIds == null || repairOrderIds.size() == 0)
        {
            throw new BusinessException("请至少选择一个维修工单");
        }
        for(int i=0; i<repairOrderIds.size(); i++)
        {
            String idStr = repairOrderIds.get(i);
            RepairOrder repairOrder = this.getById(Long.valueOf(idStr));
            if(!RepairOrderStatusConstants.REPAIRED.equals(repairOrder.getStatus()))
            {
                throw new BusinessException("工单【" + repairOrder.getOrderNumber() + "】不是已维修状态，不能确认");
            }
            repairOrder.setStatus(RepairOrderStatusConstants.CONFIRMED);
            this.update(repairOrder);
        }
    }


    @Override
    public RepairOrder findOrderByMchCode(String mchCode) {
        QueryWrapper<RepairOrder> repairOrderQueryWrapper = new QueryWrapper<>();
        repairOrderQueryWrapper.eq("mch_code", mchCode);
        repairOrderQueryWrapper.in("status", InspectionOrderStatusConstants.CREATED, InspectionOrderStatusConstants.STAGED);

        repairOrderQueryWrapper.orderByAsc("created_time");

        RepairOrder repairOrder = repairOrderMapper.selectOne(repairOrderQueryWrapper);

        return repairOrder;
    }
}
