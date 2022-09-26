package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MaintenanceOrderService {

    /**
     * 根据条件查询保养工单信息
     *
     * @return
     */
    IPage<MaintenanceOrderVO> query(Page page, MaintenanceOrderQueryParam maintenanceOrderQueryParam);



    /**
     * 更新保养工单信息
     *
     * @param maintenanceOrder
     */
    boolean update(MaintenanceOrder maintenanceOrder);

    /**
     * 根据id删除保养工单
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增保养工单
     *
     * @param maintenanceOrder
     * @return
     */
    boolean add(MaintenanceOrder maintenanceOrder);



    /**
     * 获取保养工单
     *
     * @param id
     * @return
     */
    MaintenanceOrder get(Long id);



    /**
     * 根据保养配置生成保养工单
     *
     */
    void generateMaintenanceOrder();
}
