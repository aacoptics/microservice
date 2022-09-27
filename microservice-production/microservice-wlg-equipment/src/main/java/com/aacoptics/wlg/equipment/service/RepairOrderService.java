package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
     * 根据维修配置生成维修工单
     *
     */
    void generateRepairOrder(RepairOrder repairOrder);
}
