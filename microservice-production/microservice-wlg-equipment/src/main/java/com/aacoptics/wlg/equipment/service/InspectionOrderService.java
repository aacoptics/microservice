package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderDetailVO;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface InspectionOrderService {

    /**
     * 根据条件查询点检工单信息
     *
     * @return
     */
    IPage<InspectionOrderVO> query(Page page, InspectionOrderQueryParam inspectionOrderQueryParam);

    /**
     * 根据条件查询点检工单及工单项明细信息
     *
     * @return
     */
    IPage<InspectionOrderDetailVO> queryDetail(Page page, InspectionOrderQueryParam inspectionOrderQueryParam);



    /**
     * 更新点检工单信息
     *
     * @param inspectionOrder
     */
    boolean update(InspectionOrder inspectionOrder);

    /**
     * 更新点检工单信息
     *
     * @param inspectionOrder
     */
    boolean updateById(InspectionOrder inspectionOrder);


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



    /**
     * 批量确认工单
     *
     */
    void batchConfirm(List<String> inspectionOrderIds);


    /**
     * 通过设备编码查询点检工单
     *
     * @param mchCode 设备编码
     * @return
     */
    List<InspectionOrderAndItemVO> findOrderByMchCode(String mchCode);


    /**
     * 通过用户查询点检工单
     *
     * @param user 设备编码
     * @return
     */
    List<InspectionOrderAndItemVO> findOrderByUser(String user);


    /**
     * 提交保养结果
     *
     * @param inspectionOrder
     * @return
     */
    public boolean submitOrder(InspectionOrder inspectionOrder);


    /**
     * 根据条件查询点检工单信息
     *
     * @return
     */
    List<InspectionOrderAndItemVO> queryInspectionOrderByCondition(InspectionOrderQueryParam inspectionOrderQueryParam);


    /**
     * 查找点检异常工单项
     *
     * @return
     */
    List<InspectionOrder> findInspectionExceptionOrder();

    /**
     * 查找点检超时工单责任人
     *
     * @return
     */
    List<String> findInspectionTimeoutOrderDutyPersonIdList();

    /**
     * 查找点检超时工单项
     *
     * @return
     */
    List<InspectionOrder> findInspectionTimeoutOrderByDutyPersonId(String dutyPersonId);
}
