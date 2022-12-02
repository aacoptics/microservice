package com.aacoptics.wlg.equipment.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentQueryParam extends BaseParam<Equipment> {

    /**
     * 资产编码
     */
    private String mchCode;

    /**
     * 资产名称
     */
    private String mchName;


    /**
     * 规格
     */
    private String spec;

    /**
     * 型号
     */
    private String typeVersion;

    /**
     * 出厂编码
     */
    private String factoryNo;

    /**
     * 位置编码
     */
    private String locationNo;

    /**
     * 资产管理员
     */
    private String assetManagerId;

    /**
     * 设备管理员
     */
    private String mchManagerId;

    /**
     * 责任人
     */
    private String dutyPersonId;

    /**
     * 设备编号
     */
    private String equipNumber;

    /**
     * 设备属性分类
     */
    private String equipCategory;


    /**
     * 设备状态
     */
    private String status;


    /**
     * 工段类型
     */
    private String sectionType;

}
