package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InspectionShiftService extends IService<InspectionShift> {


    /**
     * 更新点检设备信息
     *
     * @param inspectionShift
     */
    boolean update(InspectionShift inspectionShift);

    /**
     * 根据id删除点检设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增点检设备
     *
     * @param inspectionShift
     * @return
     */
    boolean add(InspectionShift inspectionShift);



    /**
     * 获取点检设备
     *
     * @param id
     * @return
     */
    InspectionShift get(Long id);


}
