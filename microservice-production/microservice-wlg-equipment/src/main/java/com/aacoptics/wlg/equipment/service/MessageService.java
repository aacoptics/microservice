package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MessageHistory;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;

import java.io.IOException;

public interface MessageService {



    /**
     * 推送点检异常工单
     * XXX（设备编号）存在XXX（保养/点检项目名称）参数异常；参数范围值为XXX（范围开始值）到XXX（范围结束值）；实际参数为XXX（保养/点检实际值）；请注意处理！
     *
     */
    boolean sendInspectionExceptionMessage();


    /**
     * 推送保养异常工单
     *
     * @return
     */
    boolean sendMaintenanceExceptionMessage();

    /**
     * 推送维修工单
     *
     */
    boolean sendRepairMessage(RepairOrder repairOrder);

    /**
     * 推送当前设备所有需要维修工单
     *
     */
    boolean sendEquipmentAllRepairMessage(RepairOrder repairOrder);


    /**
     * 推送点检超时工单
     *
     * @return
     */
    boolean sendInspectionTimeoutMessage();



    /**
     * 推送保养超时工单
     *
     * @return
     */
    boolean sendMaintenanceTimeoutMessage();



    /**
     * 推送工段工单数统计群消息
     *
     * @return
     */
    boolean sendSectionOrderCountMessage();

}
