package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_equipment")
public class Equipment extends BasePo {

    /**
     * 资产编码
     */
    @TableField(value = "mch_code")
    private String mchCode;

    /**
     * 资产名称
     */
    @TableField(value = "mch_name")
    private String mchName;

    /**
     * 规格
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 型号
     */
    @TableField(value = "type_version")
    private String typeVersion;

    /**
     * 设备状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 资产使用性质编码
     */
    @TableField(value = "asset_general_code")
    private String assetGeneralCode;

    /**
     * 资产使用性质名称
     */
    @TableField(value = "asset_general_desc")
    private String assetGeneralDesc;

    /**
     * 资产分类别编码
     */
    @TableField(value = "asset_classify_code")
    private String assetClassifyCode;

    /**
     * 资产分类别名称
     */
    @TableField(value = "asset_classify_desc")
    private String assetClassifyDesc;

    /**
     * 专业大类编码
     */
    @TableField(value = "major_big_code")
    private String majorBigCode;

    /**
     * 专业大类名称
     */
    @TableField(value = "major_big_desc")
    private String majorBigDesc;

    /**
     * 专业小类编码
     */
    @TableField(value = "major_small_code")
    private String majorSmallCode;

    /**
     * 专业小类名称
     */
    @TableField(value = "major_small_desc")
    private String majorSmallDesc;

    /**
     * 出厂编码
     */
    @TableField(value = "factory_no")
    private String factoryNo;

    /**
     * 位置编码
     */
    @TableField(value = "location_no")
    private String locationNo;

    /**
     * 地区编码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 地区名称
     */
    @TableField(value = "area_name")
    private String areaName;

    /**
     * 厂区编码
     */
    @TableField(value = "factory_id")
    private String factoryId;

    /**
     * 厂区名称
     */
    @TableField(value = "factory_name")
    private String factoryName;

    /**
     * 楼栋编码
     */
    @TableField(value = "building_id")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @TableField(value = "building_name")
    private String buildingName;

    /**
     * 楼层编码
     */
    @TableField(value = "floor_code")
    private String floorCode;

    /**
     * 楼层名称
     */
    @TableField(value = "floor_name")
    private String floorName;

    /**
     * 资产管理员
     */
    @TableField(value = "asset_manager_id")
    private String assetManagerId;

    /**
     * 设备管理员
     */
    @TableField(value = "mch_manager_id")
    private String mchManagerId;

    /**
     * 责任人
     */
    @TableField(value = "duty_person_id")
    private String dutyPersonId;

    /**
     * 责任人联系方式
     */
    @TableField(value = "duty_person_connect")
    private String dutyPersonConnect;

    /**
     * 部门经理
     */
    @TableField(value = "dept_manager_id")
    private String deptManagerId;

    /**
     * 部门总监
     */
    @TableField(value = "dept_director_id")
    private String deptDirectorId;

    /**
     * 副总裁
     */
    @TableField(value = "vice_president_id")
    private String vicePresidentId;


    /**
     * 资产状态
     */
    @TableField(value = "equip_state")
    private String equipState;


    /**
     * 资产状态
     */
    @TableField(value = "equip_state_db")
    private String equipStateDb;

    /**
     * 设备编号
     */
    @TableField(value = "equip_number")
    private String equipNumber;

    /**
     * 最后点检时间
     */
    @TableField(value = "last_inspection_datetime")
    private LocalDateTime lastInspectionDatetime;

    /**
     * 最后保养时间
     */
    @TableField(value = "last_maintenance_datetime")
    private LocalDateTime lastMaintenanceDatetime;

    /**
     * 设备负责人
     */
    @TableField(value = "equip_duty")
    private String equipDuty;

}
