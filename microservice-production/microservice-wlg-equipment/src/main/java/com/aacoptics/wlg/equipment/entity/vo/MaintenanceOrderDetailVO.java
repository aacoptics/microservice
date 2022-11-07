package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceOrderDetailVO extends BasePo {


    private String orderNumber;

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
     * 保养日期
     */
    private LocalDate maintenanceDate;

    /**
     * 保养周期
     */
    private String maintenancePeriod;


    /**
     * 保养周期单位
     */
    private String periodUnit;


    /**
     * 责任人
     */
    private String dutyPersonId;

    /**
     * 状态
     */
    private String status;

    /**
     * 设备编号
     */
    private String equipNumber;




    /**
     * 保养项
     */
    @TableField(value = "maintenance_item")
    private String maintenanceItem;

    /**
     * 保养项标准
     */
    @TableField(value = "maintenance_item_standard")
    private String maintenanceItemStandard;

    /**
     * 最小值
     */
    @TableField(value = "min_value")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @TableField(value = "max_value")
    private BigDecimal maxValue;

    /**
     * 实际值
     */
    @TableField(value = "actual_value")
    private BigDecimal actualValue;

    /**
     * 保养结果
     */
    @TableField(value = "maintenance_result")
    private String maintenanceResult;


    /**
     * 是否异常
     */
    @TableField(value = "is_exception")
    private Integer isException;

    /**
     * 是否完成
     */
    @TableField(value = "is_finish")
    private Integer isFinish;

    /**
     * 是否故障
     */
    @TableField(value = "is_fault")
    private Integer isFault;


    /**
     * 故障描述
     */
    @TableField(value = "fault_desc")
    private String faultDesc;

    /**
     * 故障照片
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "fault_image_id")
    private Long faultImageId;

    /**
     * 是否返修
     */
    @TableField(value = "is_repair")
    private Integer isRepair;



}
