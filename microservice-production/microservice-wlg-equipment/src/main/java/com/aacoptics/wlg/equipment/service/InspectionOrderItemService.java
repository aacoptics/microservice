package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;

import java.util.List;

public interface InspectionOrderItemService {


    /**
     * 批量保存
     *
     * @param inspectionOrderItemList
     * @return
     */
    boolean saveBatch(List<InspectionOrderItem> inspectionOrderItemList);

    /**
     * 更新点检工单项信息
     *
     * @param inspectionOrderItem
     */
    boolean update(InspectionOrderItem inspectionOrderItem);

    /**
     * 根据id删除点检工单项
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增点检工单项
     *
     * @param inspectionOrderItem
     * @return
     */
    boolean add(InspectionOrderItem inspectionOrderItem);



    /**
     * 获取点检工单项
     *
     * @param id
     * @return
     */
    InspectionOrderItem get(Long id);


}
