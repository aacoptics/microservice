package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface MaintenanceMainService {

    /**
     * 根据条件查询保养信息
     *
     * @return
     */
    IPage<MaintenanceMain> query(Page page, MaintenanceQueryParam maintenanceQueryParam);


    /**
     * 根据条件查询保养信息
     * @param maintenanceQueryParam
     * @return
     */
    List<MaintenanceMain> queryMaintenanceDataByCondition(MaintenanceQueryParam maintenanceQueryParam);

    /**
     * 更新保养设备信息
     *
     * @param maintenanceMain
     */
    boolean update(MaintenanceMain maintenanceMain);

    /**
     * 根据id删除保养设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增保养设备
     *
     * @param maintenanceMain
     * @return
     */
    boolean add(MaintenanceMain maintenanceMain);



    /**
     * 获取保养设备
     *
     * @param id
     * @return
     */
    MaintenanceMain get(Long id);



}
