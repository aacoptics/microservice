package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface InspectionMainService extends IService<InspectionMain> {

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
     * 解析点检Excel数据，并保存到数据库
     *
     * @param in
     * @throws IOException
     * @throws InvalidFormatException
     */
    void importInspectionExcel(InputStream in) throws Exception;


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


    /**
     * 获取点检设备
     *
     * @param mchName
     * @param spec
     * @param typeVersion
     * @return
     */
    InspectionMain getByMchNameAndSpecAndTypeVersion(String mchName, String spec, String typeVersion);


    /**
     * 获取点检项
     *
     * @param maintenanceMainId
     * @param maintenanceItem
     * @return
     */
    InspectionItem getItemByMaintenanceItemName(Long maintenanceMainId, String maintenanceItem);

    /**
     * 获取点检班次
     *
     * @param maintenanceMainId
     * @param shift
     * @return
     */
    InspectionShift getInspectionShiftByShift(Long maintenanceMainId, String shift);

}
