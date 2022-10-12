package com.aacoptics.wlg.equipment.handle;

import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SchedulerHandle {

    @Resource
    EquipmentService equipmentService;

    @Resource
    InspectionOrderService inspectionOrderService;

    @Resource
    MaintenanceOrderService maintenanceOrderService;


    @XxlJob("syncWlgEquipmentFromEAMHandle")
    public void syncWlgEquipmentFromEAMHandle() {
        try {
            equipmentService.syncWlgEquipmentFromEAM();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    @XxlJob("generateInspectionOrderHandle")
    public void generateInspectionOrderHandle() {
        try {
            inspectionOrderService.generateInspectionOrder();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    @XxlJob("generateMaintenanceOrderHandle")
    public void generateMaintenanceOrderHandle() {
        try {
            maintenanceOrderService.generateMaintenanceOrder();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}