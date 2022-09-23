package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;

public interface MaintenanceItemService {


    /**
     * 更新保养设备信息
     *
     * @param maintenanceItem
     */
    boolean update(MaintenanceItem maintenanceItem);

    /**
     * 根据id删除保养设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增保养设备
     *
     * @param maintenanceItem
     * @return
     */
    boolean add(MaintenanceItem maintenanceItem);



    /**
     * 获取保养设备
     *
     * @param id
     * @return
     */
    MaintenanceItem get(Long id);


}
