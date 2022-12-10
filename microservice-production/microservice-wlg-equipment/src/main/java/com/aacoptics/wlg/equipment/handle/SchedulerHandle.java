package com.aacoptics.wlg.equipment.handle;

import com.aacoptics.wlg.equipment.service.EquipmentService;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
import com.aacoptics.wlg.equipment.service.MessageService;
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

    @Resource
    MessageService messageService;


    /**
     * 从EAM同步WLG设备清单
     */
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

    /**
     * 生成点检工单
     */
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

    /**
     * 生成保养工单
     */
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

    /**
     * 推送点检存在异常的工单消息
     */
    @XxlJob("sendInspectionExceptionMessage")
    public void sendInspectionExceptionMessage()
    {
        try {
            messageService.sendInspectionExceptionMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 推送保养存在异常的工单消息
     */
    @XxlJob("sendMaintenanceExceptionMessage")
    public void sendMaintenanceExceptionMessage()
    {
        try {
            messageService.sendMaintenanceExceptionMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 推送点检超时的工单消息
     */
    @XxlJob("sendInspectionTimeoutMessage")
    public void sendInspectionTimeoutMessage()
    {
        try {
            messageService.sendInspectionTimeoutMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }


    /**
     * 推送保养超时的工单消息
     */
    @XxlJob("sendMaintenanceTimeoutMessage")
    public void sendMaintenanceTimeoutMessage()
    {
        try {
            messageService.sendMaintenanceTimeoutMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }


    /**
     * 推送工段工单数统计群消息
     */
    @XxlJob("sendSectionOrderCountMessage")
    public void sendSectionOrderCountMessage()
    {
        try {
            messageService.sendSectionOrderCountMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}