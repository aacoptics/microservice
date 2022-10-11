package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
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
     * 解析保养Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importMaintenanceExcel(InputStream in) throws Exception;

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


    /**
     * 获取保养设备
     *
     * @param mchName
     * @param spec
     * @param typeVersion
     * @return
     */
    MaintenanceMain getByMchNameAndSpecAndTypeVersion(String mchName, String spec, String typeVersion);


    /**
     * 获取保养项
     *
     * @param maintenanceMainId
     * @param maintenanceItem
     * @return
     */
    MaintenanceItem getItemByMaintenanceItemName(Long maintenanceMainId, String maintenanceItem);
}
