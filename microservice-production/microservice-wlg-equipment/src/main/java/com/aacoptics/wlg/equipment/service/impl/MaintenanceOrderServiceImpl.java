package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.constant.MaintenanceOrderStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
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


    @Override
    public IPage<MaintenanceOrderVO> query(Page page, MaintenanceOrderQueryParam maintenanceOrderQueryParam) {

        IPage<MaintenanceOrderVO> maintenanceOrderVOIPage = maintenanceOrderMapper.findMaintenanceOrderList(page, maintenanceOrderQueryParam);

        return maintenanceOrderVOIPage;
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

    @Override
    public boolean update(MaintenanceOrder maintenanceOrder) {

        boolean isSuccess = this.updateById(maintenanceOrder);
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
                MaintenanceOrder maintenanceOrder = new MaintenanceOrder();
                maintenanceOrder.setOrderNumber(this.getNextOrderNumber(currentDateStr));
                maintenanceOrder.setMchCode(equipment.getMchCode());
                maintenanceOrder.setDutyPersonId(equipment.getDutyPersonId());
                maintenanceOrder.setMaintenanceDate(currentDate);
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
}
