package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderDetailVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface MaintenanceOrderService {

    /**
     * 根据条件查询保养工单信息
     *
     * @return
     */
    IPage<MaintenanceOrderVO> query(Page page, MaintenanceOrderQueryParam maintenanceOrderQueryParam);


    /**
     * 根据条件查询保养工单信息
     *
     * @return
     */
    IPage<MaintenanceOrderDetailVO> queryDetail(Page page, MaintenanceOrderQueryParam maintenanceOrderQueryParam);


    /**
     * 更新保养工单及保养项信息
     *
     * @param maintenanceOrder
     */
    boolean update(MaintenanceOrder maintenanceOrder);

    /**
     * 更新保养工单信息
     *
     * @param inspectionOrder
     */
    boolean updateById(MaintenanceOrder maintenanceOrder);

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


    /**
     * 获取保养工单下一个流水号
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
    void batchConfirm(List<String> maintenanceOrderIds);


    /**
     * 通过设备编码查询保养工单
     *
     * @param mchCode 设备编码
     * @return
     */
    MaintenanceOrderAndItemVO findOrderByMchCode(String mchCode);

    /**
     * 提交保养结果
     *
     * @param maintenanceOrder
     * @return
     */
    public boolean submitOrder(MaintenanceOrder maintenanceOrder);


    /**
     * 根据条件查询保养工单信息
     *
     * @return
     */
    List<MaintenanceOrderAndItemVO> queryMaintenanceOrderByCondition(MaintenanceOrderQueryParam maintenanceOrderQueryParam);



    /**
     * 查找保养异常工单
     *
     * @return
     */
    List<MaintenanceOrder> findMaintenanceExceptionOrder();


    /**
     * 查找b保养时工单责任人
     *
     * @return
     */
    List<String> findMaintenanceTimeoutOrderDutyPersonIdList();

    /**
     * 查找保养超时工单项
     *
     * @return
     */
    List<MaintenanceOrder> findMaintenanceTimeoutOrderByDutyPersonId(String dutyPersonId);
}
