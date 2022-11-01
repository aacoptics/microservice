package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.EquipmentStatusConstants;
import com.aacoptics.wlg.equipment.constant.InspectionOrderStatusConstants;
import com.aacoptics.wlg.equipment.constant.MaintenanceOrderStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MaintenanceOrderItemMapper;
import com.aacoptics.wlg.equipment.mapper.MaintenanceOrderMapper;
import com.aacoptics.wlg.equipment.service.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MaintenanceOrderServiceImpl extends ServiceImpl<MaintenanceOrderMapper, MaintenanceOrder> implements MaintenanceOrderService {

    @Resource
    MaintenanceOrderMapper maintenanceOrderMapper;

    @Resource
    MaintenanceOrderItemMapper maintenanceOrderItemMapper;

    @Resource
    EquipmentService equipmentService;

    @Resource
    MaintenanceMainService maintenanceMainService;

    @Resource
    MaintenanceOrderItemService maintenanceOrderItemService;

    @Resource
    SequenceService sequenceService;

    @Resource
    RepairOrderService repairOrderService;


    @Override
    public IPage<MaintenanceOrderVO> query(Page page, MaintenanceOrderQueryParam maintenanceOrderQueryParam) {

        IPage<MaintenanceOrderVO> maintenanceOrderVOIPage = maintenanceOrderMapper.findMaintenanceOrderList(page, maintenanceOrderQueryParam);

        return maintenanceOrderVOIPage;
    }



    @Override
    public List<MaintenanceOrderAndItemVO> queryMaintenanceOrderByCondition(MaintenanceOrderQueryParam maintenanceOrderQueryParam)
    {
        List<MaintenanceOrderAndItemVO> maintenanceOrderAndItemList = maintenanceOrderMapper.findMaintenanceOrderAndItemList(maintenanceOrderQueryParam);

        return maintenanceOrderAndItemList;
    }

    @Override
    public boolean add(MaintenanceOrder maintenanceOrder) {
        boolean isSuccess = this.save(maintenanceOrder);
        if(isSuccess == false)
        {
            throw new BusinessException("新增保养工单失败");
        }

        List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrder.getMaintenanceOrderItemList();
        if(maintenanceOrderItemList != null && maintenanceOrderItemList.size()>0)
        {
            for(MaintenanceOrderItem maintenanceOrderItem : maintenanceOrderItemList)
            {
                maintenanceOrderItem.setMaintenanceOrderId(maintenanceOrder.getId());
                isSuccess = maintenanceOrderItemService.add(maintenanceOrderItem);
                if(isSuccess == false)
                {
                    throw new BusinessException("新增保养工单失败");
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
    public boolean update(MaintenanceOrder maintenanceOrder) {

        boolean isSuccess = this.updateById(maintenanceOrder);
        if(isSuccess == false)
        {
            throw new BusinessException("更新工单异常");
        }

        List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrder.getMaintenanceOrderItemList();
        for(MaintenanceOrderItem maintenanceOrderItem : maintenanceOrderItemList)
        {
            isSuccess = maintenanceOrderItemService.update(maintenanceOrderItem);
            if(isSuccess == false)
            {
                throw new BusinessException("更新工单项异常");
            }
        }

        return isSuccess;
    }


    @Override
    public MaintenanceOrder get(Long id) {
        MaintenanceOrder maintenanceOrder = this.getById(id);
        if (Objects.isNull(maintenanceOrder)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        //查询保养项
        QueryWrapper<MaintenanceOrderItem> maintenanceOrderItemQueryWrapper = new QueryWrapper<MaintenanceOrderItem>();
        maintenanceOrderItemQueryWrapper.eq( "maintenance_order_id", id);

        maintenanceOrderItemQueryWrapper.orderByAsc("maintenance_item");
        List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrderItemMapper.selectList(maintenanceOrderItemQueryWrapper);

        maintenanceOrder.setMaintenanceOrderItemList(maintenanceOrderItemList);

        return maintenanceOrder;
    }


    @Transactional
    @Override
    public void generateMaintenanceOrder() {
        log.info("开始生成设备保养工单");
        List<MaintenanceMain> maintenanceMainList = maintenanceOrderMapper.findMaintenanceList();
        if(maintenanceMainList == null || maintenanceMainList.size() == 0)
        {
            log.warn("没有可用于生成保养工单的数据，请配置保养项和班次");
        }
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter orderNumberDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String currentDateStr = orderNumberDateFormatter.format(currentDate);

        for(int i=0; i<maintenanceMainList.size(); i++)
        {
            MaintenanceMain maintenanceMain = maintenanceMainList.get(i);

            String mchName = maintenanceMain.getMchName();
            String spec = maintenanceMain.getSpec();
            String typeVersion = maintenanceMain.getTypeVersion();
            Long maintenancePeriod = maintenanceMain.getMaintenancePeriod();
//            String periodUnit = maintenanceMain.getPeriodUnit();
            //获取设备
            List<Equipment> equipmentList = equipmentService.findEquipmentList(mchName, spec, typeVersion);
            if(equipmentList == null || equipmentList.size() ==0)
            {
                continue;
            }
            //获取保养项
            MaintenanceMain maintenanceMainDetail = maintenanceMainService.get(maintenanceMain.getId());
            List<MaintenanceItem> maintenanceItemList = maintenanceMainDetail.getMaintenanceItemList();
            if(maintenanceItemList == null)
            {
                continue;
            }

            for(Equipment equipment : equipmentList)
            {
                LocalDateTime lastMaintenanceDatetime = equipment.getLastMaintenanceDatetime();
                if(lastMaintenanceDatetime != null && lastMaintenanceDatetime.toLocalDate().isAfter(currentDate))
                {
                    continue;
                }
                LocalDate maintenanceDate = lastMaintenanceDatetime.toLocalDate().plusMonths(maintenancePeriod);

                MaintenanceOrder maintenanceOrder = new MaintenanceOrder();
                maintenanceOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));
                maintenanceOrder.setMchCode(equipment.getMchCode());
                //优先取设备负责人，如果为空则取责任人
                maintenanceOrder.setDutyPersonId(equipment.getEquipDuty() != null ? equipment.getEquipDuty() : equipment.getDutyPersonId());

                maintenanceOrder.setMaintenanceDate(maintenanceDate); //计划保养日期
                maintenanceOrder.setMaintenancePeriod(maintenanceMain.getMaintenancePeriod());
                maintenanceOrder.setPeriodUnit(maintenanceMain.getPeriodUnit());

                maintenanceOrder.setStatus(MaintenanceOrderStatusConstants.CREATED);

                //创建工单保养项
                List<MaintenanceOrderItem> maintenanceOrderItemList = new ArrayList<>();
                for(MaintenanceItem maintenanceItem : maintenanceItemList)
                {
                    MaintenanceOrderItem maintenanceOrderItem = new MaintenanceOrderItem();
                    maintenanceOrderItem.setMaintenanceItem(maintenanceItem.getMaintenanceItem());
                    maintenanceOrderItem.setMaintenanceItemStandard(maintenanceItem.getMaintenanceItemStandard());
                    maintenanceOrderItem.setMinValue(maintenanceItem.getMinValue());
                    maintenanceOrderItem.setMaxValue(maintenanceItem.getMaxValue());

                    maintenanceOrderItemList.add(maintenanceOrderItem);
                }
                maintenanceOrder.setMaintenanceOrderItemList(maintenanceOrderItemList);

                this.add(maintenanceOrder);

            }
        }
        log.info("完成生成设备保养工单");
    }

    @Override
    public Long getOrCreateSequenceNumber(String sequenceName) {
        Long nextSequenceNumber =  sequenceService.getNextNumberByName(sequenceName);
        if(nextSequenceNumber == null) {
            Sequence sequence = sequenceService.createSequence(sequenceName, "保养工单流水号", 1l, 1l, 999999l);
            nextSequenceNumber = sequence.getSequenceNumber();
        }
        return nextSequenceNumber;
    }

    @Override
    public String getNextOrderNumber(String currentDate) {
        String sequenceName = "MAINTENANCE_ORDER_" + currentDate;

        Long nextSequenceNumber = this.getOrCreateSequenceNumber(sequenceName);

        String nextSequenceNumberStr =  currentDate + String.format("%04d", nextSequenceNumber);

        return nextSequenceNumberStr;
    }

    @Override
    @Transactional
    public void batchConfirm(List<String> maintenanceOrderIds) {
        if(maintenanceOrderIds == null || maintenanceOrderIds.size() == 0)
        {
            throw new BusinessException("请至少选择一个维修工单");
        }
        for(int i=0; i<maintenanceOrderIds.size(); i++)
        {
            String idStr = maintenanceOrderIds.get(i);
            MaintenanceOrder maintenanceOrder = this.getById(Long.valueOf(idStr));
            if(!MaintenanceOrderStatusConstants.COMMITTED.equals(maintenanceOrder.getStatus()))
            {
                throw new BusinessException("工单【" + maintenanceOrder.getOrderNumber() + "】不是已提交状态，不能确认");
            }
            maintenanceOrder.setStatus(MaintenanceOrderStatusConstants.CONFIRMED);
            this.update(maintenanceOrder);
        }
    }


    @Override
    public MaintenanceOrderAndItemVO findOrderByMchCode(String mchCode) {
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if(equipment == null)
        {
            throw new BusinessException("设备【" + mchCode + "】不存在，请确认！");
        }

        MaintenanceOrderAndItemVO maintenanceOrderAndItemVO = maintenanceOrderMapper.findOrderByMchCode(mchCode);
        if(maintenanceOrderAndItemVO == null)
        {
            throw new BusinessException("设备【" + mchCode + "】不存在需要保养的工单，请确认！");
        }
        //查询保养项
        QueryWrapper<MaintenanceOrderItem> maintenanceOrderItemQueryWrapper = new QueryWrapper<MaintenanceOrderItem>();
        maintenanceOrderItemQueryWrapper.eq( "maintenance_order_id", maintenanceOrderAndItemVO.getId());

        maintenanceOrderItemQueryWrapper.orderByAsc("maintenance_item");
        List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrderItemMapper.selectList(maintenanceOrderItemQueryWrapper);

        maintenanceOrderAndItemVO.setMaintenanceOrderItemList(maintenanceOrderItemList);

        return maintenanceOrderAndItemVO;
    }



    @Transactional
    @Override
    public boolean submitOrder(MaintenanceOrder maintenanceOrder) {
        String orderStatus = maintenanceOrder.getStatus();
        if(!MaintenanceOrderStatusConstants.STAGED.equals(orderStatus) && !MaintenanceOrderStatusConstants.COMMITTED.equals(orderStatus))
        {
            throw new BusinessException("状态只能为1(已暂存)或2(已提交)，请确认");
        }

        Long maintenanceOrderId =  maintenanceOrder.getId();
        MaintenanceOrder targetMaintenanceOrder =  this.get(maintenanceOrderId);


        boolean isRepairBoolean = false;
        List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrder.getMaintenanceOrderItemList();
        for(MaintenanceOrderItem maintenanceOrderItem : maintenanceOrderItemList)
        {
            //判断是否需要维修
            Integer isRepair = maintenanceOrderItem.getIsRepair();
            if(isRepair == 1 && MaintenanceOrderStatusConstants.COMMITTED.equals(orderStatus))
            {
                isRepairBoolean = true;
                repairOrderService.createRepairOrderByMaintenance(targetMaintenanceOrder, maintenanceOrderItem);
            }
        }

        targetMaintenanceOrder.setStatus(orderStatus); //设置状态
        targetMaintenanceOrder.setMaintenanceOrderItemList(maintenanceOrderItemList);

        //更新设备状态
        String mchCode = targetMaintenanceOrder.getMchCode();
        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        if(isRepairBoolean) {
            equipment.setStatus(EquipmentStatusConstants.REPAIR);
        }
        equipment.setLastMaintenanceDatetime(LocalDateTime.now());
        equipmentService.update(equipment);

        boolean isSuccess = this.update(targetMaintenanceOrder);
        return isSuccess;
    }
}
