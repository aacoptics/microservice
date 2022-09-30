package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface RepairOrderService {

    /**
     * 根据条件查询维修工单信息
     *
     * @return
     */
    IPage<RepairOrderVO> query(Page page, RepairOrderQueryParam repairOrderQueryParam);



    /**
     * 更新维修工单信息
     *
     * @param repairOrder
     */
    boolean update(RepairOrder repairOrder);

    /**
     * 根据id删除维修工单
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增维修工单
     *
     * @param repairOrder
     * @return
     */
    boolean add(RepairOrder repairOrder);



    /**
     * 获取维修工单
     *
     * @param id
     * @return
     */
    RepairOrder get(Long id);




    /**
     * 获取保养工单下一个流水号
     *
     * @param sequenceName
     * @return
     */
    Long getOrCreateSequenceNumber(String sequenceName);

    /**
     * 获取下一个工单号
     *
     * @param sequenceName
     * @return
     */
    String getNextOrderNumber(String sequenceName);


    /**
     * 批量确认工单
     *
     */
    void batchConfirm(List<String> repairOrderIds);


    /**
     * 通过设备编码查询维修工单
     *
     * @param mchCode 设备编码
     * @return
     */
    RepairOrderVO findOrderByMchCode(String mchCode);


    /**
     * 提交维修工单信息
     *
     * @param repairOrder
     */
    boolean submitOrder(RepairOrder repairOrder);

    /**
     * 通过点检创建维修工单
     *
     */
    RepairOrder createRepairOrderByInspection(InspectionOrder inspectionOrder, InspectionOrderItem inspectionOrderItem);

    /**
     * 通过保养创建维修工单
     *
     */
    RepairOrder createRepairOrderByMaintenance(MaintenanceOrder maintenanceOrder, MaintenanceOrderItem maintenanceOrderItem);

}
