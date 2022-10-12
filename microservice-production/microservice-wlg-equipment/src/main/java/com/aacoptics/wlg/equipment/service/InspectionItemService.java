package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InspectionItemService extends IService<InspectionItem> {


    /**
     * 更新点检设备信息
     *
     * @param inspectionItem
     */
    boolean update(InspectionItem inspectionItem);

    /**
     * 根据id删除点检设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增点检设备
     *
     * @param inspectionItem
     * @return
     */
    boolean add(InspectionItem inspectionItem);



    /**
     * 获取点检设备
     *
     * @param id
     * @return
     */
    InspectionItem get(Long id);


}
