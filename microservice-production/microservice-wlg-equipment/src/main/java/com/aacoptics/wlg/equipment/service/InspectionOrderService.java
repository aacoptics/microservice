package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface InspectionOrderService {

    /**
     * 根据条件查询点检工单信息
     *
     * @return
     */
    IPage<InspectionOrderVO> query(Page page, InspectionOrderQueryParam inspectionOrderQueryParam);



    /**
     * 更新点检工单信息
     *
     * @param inspectionOrder
     */
    boolean update(InspectionOrder inspectionOrder);

    /**
     * 根据id删除点检工单
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增点检工单
     *
     * @param inspectionOrder
     * @return
     */
    boolean add(InspectionOrder inspectionOrder);



    /**
     * 获取点检工单
     *
     * @param id
     * @return
     */
    InspectionOrder get(Long id);



    /**
     * 根据点检配置生成点检工单
     *
     */
    void generateInspectionOrder();



    /**
     * 获取点检工单下一个流水号
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
}
