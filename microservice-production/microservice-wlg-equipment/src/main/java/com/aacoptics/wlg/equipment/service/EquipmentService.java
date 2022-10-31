package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface EquipmentService {


    /**
     * 从EAM数据库获取WLG设备
     *
     *
     * @return
     */
    List<Map<String, Object>> findWlgEquipmentByEAM();

    /**
     * 保存设备对象
     *
     * @param equipmentMap
     */
    void saveEquipment(Map<String, Object> equipmentMap);

    /**
     * 从EAM系统获取设备并保存到WLGIOT数据库
     */
    void syncWlgEquipmentFromEAM();



    /**
     * 根据条件查询设备
     *
     * @return
     */
    IPage<Equipment> query(Page page, EquipmentQueryParam equipmentQueryParam);


    /**
     * 更新设备信息
     *
     * @param equipment
     */
    boolean update(Equipment equipment);

    /**
     * 根据id删除设备
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增设备
     *
     * @param equipment
     * @return
     */
    boolean add(Equipment equipment);



    /**
     * 获取设备
     *
     * @param id
     * @return
     */
    Equipment get(Long id);


    /**
     * 去重查询所有设备名称
     *
     * @return
     */
    List<String> findMchNameList();


    /**
     * 查询设备名称下的所有规格
     *
     * @param equipmentQueryParam
     * @return
     */
    List<String> findSpecListByMchName(EquipmentQueryParam equipmentQueryParam);


    /**
     * 根据设备名称和规格查询所有型号
     *
     * @param equipmentQueryParam
     * @return
     */
    List<String> findTypeVersionListByMchNameAndSpec(EquipmentQueryParam equipmentQueryParam);

    /**
     * 根据设备名称，规格，型号查询设备
     *
     * @param mchName 设备名称
     * @param spec 规格
     * @param typeVersion 型号
     *
     * @return
     */
    List<Equipment> findEquipmentList(String mchName, String spec, String typeVersion);

    /**
     * 根据设备名称，规格，型号查询设备数量
     *
     * @param mchName 设备名称
     * @param spec 规格
     * @param typeVersion 型号
     *
     * @return
     */
    Integer findEquipmentCountList(String mchName, String spec, String typeVersion);


    /**
     * 根据设备编码或编号查询设备
     *
     * @param mchCode 设备编码
     * @return
     */
    Equipment findEquipmentByMchCode(String mchCode);

    /**
     * 查询设备清单
     * @param equipmentQueryParam
     * @return
     */
    public List<Equipment> queryEquipmentByCondition(EquipmentQueryParam equipmentQueryParam);
}
