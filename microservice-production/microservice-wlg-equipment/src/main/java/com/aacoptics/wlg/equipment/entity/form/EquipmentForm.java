package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel
@Data
public class EquipmentForm extends BaseForm<Equipment> {

    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    private String mchCode;

    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称")
    private String mchName;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String spec;

    /**
     * 型号
     */
    @ApiModelProperty(value = "型号")
    private String typeVersion;

    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    private String status;

    /**
     * 资产使用性质编码
     */
    @ApiModelProperty(value = "资产使用性质编码")
    private String assetGeneralCode;

    /**
     * 资产使用性质名称
     */
    @ApiModelProperty(value = "资产使用性质名称")
    private String assetGeneralDesc;

    /**
     * 资产分类别编码
     */
    @ApiModelProperty(value = "资产分类别编码")
    private String assetClassifyCode;

    /**
     * 资产分类别名称
     */
    @ApiModelProperty(value = "资产分类别名称")
    private String assetClassifyDesc;

    /**
     * 专业大类编码
     */
    @ApiModelProperty(value = "专业大类编码")
    private String majorBigCode;

    /**
     * 专业大类名称
     */
    @ApiModelProperty(value = "专业大类名称")
    private String majorBigDesc;

    /**
     * 专业小类编码
     */
    @ApiModelProperty(value = "专业小类编码")
    private String majorSmallCode;

    /**
     * 专业小类名称
     */
    @ApiModelProperty(value = "专业小类名称")
    private String majorSmallDesc;

    /**
     * 出厂编码
     */
    @ApiModelProperty(value = "出厂编码")
    private String factoryNo;

    /**
     * 位置编码
     */
    @ApiModelProperty(value = "位置编码")
    private String locationNo;

    /**
     * 地区编码
     */
    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    /**
     * 地区名称
     */
    @ApiModelProperty(value = "地区名称")
    private String areaName;

    /**
     * 厂区编码
     */
    @ApiModelProperty(value = "厂区编码")
    private String factoryId;

    /**
     * 厂区名称
     */
    @ApiModelProperty(value = "厂区名称")
    private String factoryName;

    /**
     * 楼栋编码
     */
    @ApiModelProperty(value = "楼栋编码")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    /**
     * 楼层编码
     */
    @ApiModelProperty(value = "楼层编码")
    private String floorCode;

    /**
     * 楼层名称
     */
    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    /**
     * 资产管理员
     */
    @ApiModelProperty(value = "资产管理员")
    private String assetManagerId;

    /**
     * 设备管理员
     */
    @ApiModelProperty(value = "设备管理员")
    private String mchManagerId;

    /**
     * 责任人
     */
    @ApiModelProperty(value = "责任人")
    private String dutyPersonId;

    /**
     * 责任人联系方式
     */
    @ApiModelProperty(value = "责任人联系方式")
    private String dutyPersonConnect;

    /**
     * 部门经理
     */
    @ApiModelProperty(value = "部门经理")
    private String deptManagerId;

    /**
     * 部门总监
     */
    @ApiModelProperty(value = "部门总监")
    private String deptDirectorId;

    /**
     * 最后点检时间
     */
    @ApiModelProperty(value = "最后点检时间")
    private LocalDateTime lastInspectionDatetime;

    /**
     * 最后保养时间
     */
    @TableField(value = "last_maintenance_datetime")
    private LocalDateTime lastMaintenanceDatetime;

}
