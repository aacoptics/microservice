package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface InspectionMainService {

    /**
     * 根据条件查询点检信息
     *
     * @return
     */
    IPage<InspectionMain> query(Page page, InspectionQueryParam inspectionQueryParam);



    /**
     * 根据条件查询点检信息
     * @param inspectionQueryParam
     * @return
     */
    List<InspectionMain> queryInspectionDataByCondition(InspectionQueryParam inspectionQueryParam);

    /**
     * 更新点检设备信息
     *
     * @param inspectionMain
     */
    boolean update(InspectionMain inspectionMain);

    /**
     * 根据id删除点检设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增点检设备
     *
     * @param inspectionMain
     * @return
     */
    boolean add(InspectionMain inspectionMain);



    /**
     * 获取点检设备
     *
     * @param id
     * @return
     */
    InspectionMain get(Long id);


}
