package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;

import java.util.List;

public interface MaintenanceOrderItemService {


    /**
     * 批量保存
     *
     * @param maintenanceOrderItemList
     * @return
     */
    boolean saveBatch(List<MaintenanceOrderItem> maintenanceOrderItemList);

    /**
     * 更新保养工单项信息
     *
     * @param maintenanceOrderItem
     */
    boolean update(MaintenanceOrderItem maintenanceOrderItem);

    /**
     * 根据id删除保养工单项
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增保养工单项
     *
     * @param maintenanceOrderItem
     * @return
     */
    boolean add(MaintenanceOrderItem maintenanceOrderItem);



    /**
     * 获取保养工单项
     *
     * @param id
     * @return
     */
    MaintenanceOrderItem get(Long id);


}
