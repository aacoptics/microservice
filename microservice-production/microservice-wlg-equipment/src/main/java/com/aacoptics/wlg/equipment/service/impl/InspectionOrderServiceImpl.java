package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.*;
import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderDetailVO;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.InspectionOrderItemMapper;
import com.aacoptics.wlg.equipment.mapper.InspectionOrderMapper;
import com.aacoptics.wlg.equipment.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InspectionOrderServiceImpl extends ServiceImpl<InspectionOrderMapper, InspectionOrder> implements InspectionOrderService {

    @Resource
    InspectionOrderMapper inspectionOrderMapper;

    @Resource
    InspectionOrderItemMapper inspectionOrderItemMapper;

    @Resource
    EquipmentService equipmentService;

    @Resource
    InspectionMainService inspectionMainService;

    @Resource
    InspectionOrderItemService inspectionOrderItemService;

    @Resource
    SequenceService sequenceService;

    @Resource
    RepairOrderService repairOrderService;

    @Override
    public IPage<InspectionOrderVO> query(Page page, InspectionOrderQueryParam inspectionOrderQueryParam) {

        IPage<InspectionOrderVO> inspectionOrderVOIPage = inspectionOrderMapper.findInspectionOrderList(page, inspectionOrderQueryParam);

        return inspectionOrderVOIPage;
    }

    @Override
    public IPage<InspectionOrderDetailVO> queryDetail(Page page, InspectionOrderQueryParam inspectionOrderQueryParam) {

        IPage<InspectionOrderDetailVO> inspectionOrderDetailList = inspectionOrderMapper.findInspectionOrderDetailList(page, inspectionOrderQueryParam);

        return inspectionOrderDetailList;
    }

    @Override
    public List<InspectionOrderAndItemVO> queryInspectionOrderByCondition(InspectionOrderQueryParam inspectionOrderQueryParam)
    {
        List<InspectionOrderAndItemVO> inspectionOrderAndItemVOList = inspectionOrderMapper.findInspectionOrderAndItemList(inspectionOrderQueryParam);

        return inspectionOrderAndItemVOList;
    }

    @Override
    public boolean add(InspectionOrder inspectionOrder) {
        boolean isSuccess = this.save(inspectionOrder);
        if(isSuccess == false)
        {
            throw new BusinessException("新增点检工单失败");
        }

        List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();
        if(inspectionOrderItemList != null && inspectionOrderItemList.size()>0)
        {
            for(InspectionOrderItem inspectionOrderItem : inspectionOrderItemList)
            {
                inspectionOrderItem.setInspectionOrderId(inspectionOrder.getId());
                isSuccess = inspectionOrderItemService.add(inspectionOrderItem);
                if(isSuccess == false)
                {
                    throw new BusinessException("新增点检工单失败");
                }
            }
        }

        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Transactional
    @Override
    public boolean update(InspectionOrder inspectionOrder) {

        boolean isSuccess = this.updateById(inspectionOrder);
        if(isSuccess == false)
        {
            throw new BusinessException("更新工单异常");
        }
        List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();
        if(inspectionOrderItemList != null) {
            for (InspectionOrderItem inspectionOrderItem : inspectionOrderItemList) {
                isSuccess = inspectionOrderItemService.update(inspectionOrderItem);
                if (isSuccess == false) {
                    throw new BusinessException("更新工单项异常");
                }
            }
        }
        return isSuccess;
    }

    @Override
    public boolean updateById(InspectionOrder inspectionOrder) {
        return super.updateById(inspectionOrder);
    }

    @Override
    public InspectionOrder get(Long id) {
        InspectionOrder inspectionOrder = this.getById(id);
        if (Objects.isNull(inspectionOrder)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        //查询点检项
        QueryWrapper<InspectionOrderItem> inspectionOrderItemQueryWrapper = new QueryWrapper<InspectionOrderItem>();
        inspectionOrderItemQueryWrapper.eq( "inspection_order_id", id);

        inspectionOrderItemQueryWrapper.orderByAsc("check_item");
        List<InspectionOrderItem> inspectionOrderItemList = inspectionOrderItemMapper.selectList(inspectionOrderItemQueryWrapper);

        inspectionOrder.setInspectionOrderItemList(inspectionOrderItemList);

        return inspectionOrder;
    }


    @Transactional
    @Override
    public void generateInspectionOrder() {
        log.info("开始生成设备点检工单");
        List<InspectionMain> inspectionMainList = inspectionOrderMapper.findInspectionList();
        if(inspectionMainList == null || inspectionMainList.size() == 0)
        {
            log.warn("没有可用于生成点检工单的数据，请配置点检项和班次");
        }
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String currentDateStr = currentDate.format(dateFormatter);
        String orderNumberDateStr = orderNumberDateFormatter.format(currentDate);

        for(int i=0; i<inspectionMainList.size(); i++)
        {
            InspectionMain inspectionMain = inspectionMainList.get(i);

            String mchName = inspectionMain.getMchName();
            String spec = inspectionMain.getSpec();
            String typeVersion = inspectionMain.getTypeVersion();
            //获取设备
            List<Equipment> equipmentList = equipmentService.findEquipmentList(mchName, spec, typeVersion);
            if(equipmentList == null || equipmentList.size() ==0)
            {
                continue;
            }
            //获取点检班次和点检项
            InspectionMain inspectionMainDetail = inspectionMainService.get(inspectionMain.getId());
            List<InspectionItem> inspectionItemList = inspectionMainDetail.getInspectionItemList();
            List<InspectionShift> inspectionShiftList = inspectionMainDetail.getInspectionShiftList();
            if(inspectionItemList == null || inspectionShiftList == null)
            {
                continue;
            }

            for(InspectionShift inspectionShift : inspectionShiftList)
            {
                for(Equipment equipment : equipmentList)
                {
                    InspectionOrder inspectionOrder = new InspectionOrder();
                    inspectionOrder.setOrderNumber(this.getNextOrderNumber(orderNumberDateStr));
                    inspectionOrder.setMchCode(equipment.getMchCode());
                    //优先取设备负责人，如果为空则取责任人
                    inspectionOrder.setDutyPersonId(StringUtils.isNotEmpty(equipment.getEquipDuty()) ? equipment.getEquipDuty() : equipment.getDutyPersonId());
                    inspectionOrder.setInspectionDate(currentDate);
                    inspectionOrder.setInspectionShift(inspectionShift.getShift());

                    LocalDateTime shiftStartTime = LocalDateTime.parse(currentDateStr + " " + inspectionShift.getStartTime(), dateTimeFormatter);
                    LocalDateTime shiftEndTime = LocalDateTime.parse(currentDateStr + " " + inspectionShift.getStartTime(), dateTimeFormatter);

                    inspectionOrder.setShiftStartTime(shiftStartTime);
                    inspectionOrder.setShiftEndTime(shiftEndTime);
                    inspectionOrder.setStatus(InspectionOrderStatusConstants.CREATED);
                    inspectionOrder.setExceptionNotification(NotificationStatusConstants.NO);
                    inspectionOrder.setTimeoutNotification(NotificationStatusConstants.NO);
                    //创建工单点检项
                    List<InspectionOrderItem> inspectionOrderItemList = new ArrayList<>();
                    for(InspectionItem inspectionItem : inspectionItemList)
                    {
                        InspectionOrderItem inspectionOrderItem = new InspectionOrderItem();
                        inspectionOrderItem.setCheckItem(inspectionItem.getCheckItem());
                        inspectionOrderItem.setCheckItemStandard(inspectionItem.getCheckItemStandard());
                        inspectionOrderItem.setMinValue(inspectionItem.getMinValue());
                        inspectionOrderItem.setMaxValue(inspectionItem.getMaxValue());

                        inspectionOrderItemList.add(inspectionOrderItem);
                    }
                    inspectionOrder.setInspectionOrderItemList(inspectionOrderItemList);

                    this.add(inspectionOrder);
                }
            }
        }
        log.info("完成生成设备点检工单");
    }


    @Override
    public Long getOrCreateSequenceNumber(String sequenceName) {
        Long nextSequenceNumber =  sequenceService.getNextNumberByName(sequenceName);
        if(nextSequenceNumber == null) {
            Sequence sequence = sequenceService.createSequence(sequenceName, "点检工单流水号", 1l, 1l, 999999l);
            nextSequenceNumber = sequence.getSequenceNumber();
        }
        return nextSequenceNumber;
    }

    @Override
    public String getNextOrderNumber(String currentDate) {
        String sequenceName = "INSPECTION_ORDER_" + currentDate;

        Long nextSequenceNumber = this.getOrCreateSequenceNumber(sequenceName);

        String nextSequenceNumberStr =  currentDate + String.format("%04d", nextSequenceNumber);

        return nextSequenceNumberStr;
    }


    @Override
    @Transactional
    public void batchConfirm(List<String> inspectionOrderIds) {
        if(inspectionOrderIds == null || inspectionOrderIds.size() == 0)
        {
            throw new BusinessException("请至少选择一个点检工单");
        }
        for(int i=0; i<inspectionOrderIds.size(); i++)
        {
            String idStr = inspectionOrderIds.get(i);
            InspectionOrder inspectionOrder = this.get(Long.valueOf(idStr));
            if(!InspectionOrderStatusConstants.COMMITTED.equals(inspectionOrder.getStatus()))
            {
                throw new BusinessException("工单【" + inspectionOrder.getOrderNumber() + "】不是已提交状态，不能确认");
            }
            inspectionOrder.setStatus(InspectionOrderStatusConstants.CONFIRMED);
            this.updateById(inspectionOrder);
        }
    }

    @Override
    public List<InspectionOrderAndItemVO> findOrderByMchCode(String mchCode) {
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if(equipment == null)
        {
            throw new BusinessException("设备【" + mchCode + "】不存在，请确认！");
        }

        List<InspectionOrderAndItemVO> inspectionOrderAndItemVOList = inspectionOrderMapper.findOrderByMchCode(mchCode);
        if(inspectionOrderAndItemVOList == null || inspectionOrderAndItemVOList.size() == 0)
        {
            throw new BusinessException("设备【" + mchCode + "】不存在需要点检的工单，请确认！");
        }
        return inspectionOrderAndItemVOList;
    }


    @Transactional
    @Override
    public boolean submitOrder(InspectionOrder inspectionOrder) {
        String orderStatus = inspectionOrder.getStatus();

        InspectionOrder targetInspectionOrder = this.get(inspectionOrder.getId());

        String mchCode = targetInspectionOrder.getMchCode();
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if(!InspectionOrderStatusConstants.STAGED.equals(orderStatus) && !InspectionOrderStatusConstants.COMMITTED.equals(orderStatus))
        {
            throw new BusinessException("状态只能为1(已暂存)或2(已提交)，请确认");
        }

        List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();
        boolean isRepairBoolean = false;
        for(InspectionOrderItem inspectionOrderItem : inspectionOrderItemList)
        {
            //判断是否需要维修
            Integer isRepair = inspectionOrderItem.getIsRepair();
            if(isRepair == 1 && InspectionOrderStatusConstants.COMMITTED.equals(orderStatus))
            {
                isRepairBoolean = true;
                repairOrderService.createRepairOrderByInspection(targetInspectionOrder, inspectionOrderItem);
            }
        }

        targetInspectionOrder.setStatus(orderStatus);
        targetInspectionOrder.setInspectionOrderItemList(inspectionOrderItemList);


        //更新设备状态
        if(isRepairBoolean) {
            equipment.setStatus(EquipmentStatusConstants.REPAIR);
        }
        equipment.setLastInspectionDatetime(LocalDateTime.now());
        equipmentService.update(equipment);


        boolean isSuccess = this.updateById(targetInspectionOrder);
        return isSuccess;
    }

    @Override
    public List<InspectionOrder> findInspectionExceptionOrder() {
        QueryWrapper<InspectionOrder> inspectionOrderQueryWrapper = new QueryWrapper<>();
        inspectionOrderQueryWrapper.eq("exception_notification", NotificationStatusConstants.NO);
        inspectionOrderQueryWrapper.in("status", InspectionOrderStatusConstants.COMMITTED, InspectionOrderStatusConstants.CONFIRMED);
        inspectionOrderQueryWrapper.inSql("id","select inspection_order_id from em_inspection_order_item where is_exception = 1");
        inspectionOrderQueryWrapper.orderByAsc("order_number");
        List<InspectionOrder> inspectionOrderList = this.list(inspectionOrderQueryWrapper);

        return inspectionOrderList;
    }

    @Override
    public List<String> findInspectionTimeoutOrderDutyPersonIdList() {
        List<String> dutyPersonIdList = inspectionOrderMapper.findInspectionTimeoutOrderDutyPersonIdList();
        return dutyPersonIdList;
    }

    @Override
    public List<InspectionOrder> findInspectionTimeoutOrderByDutyPersonId(String dutyPersonId) {
        QueryWrapper<InspectionOrder> inspectionOrderQueryWrapper = new QueryWrapper<>();
        inspectionOrderQueryWrapper.eq("duty_person_id", dutyPersonId);
        inspectionOrderQueryWrapper.eq("timeout_notification", NotificationStatusConstants.NO);
        inspectionOrderQueryWrapper.eq("status", InspectionOrderStatusConstants.CREATED);
        inspectionOrderQueryWrapper.lt("shift_end_time", LocalDateTime.now());
        inspectionOrderQueryWrapper.orderByAsc("order_number");
        List<InspectionOrder> inspectionOrderList = this.list(inspectionOrderQueryWrapper);

        return inspectionOrderList;
    }
}
