package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_maintenance_order_item")
public class MaintenanceOrderItem extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "maintenance_order_id")
    private Long maintenanceOrderId;


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
