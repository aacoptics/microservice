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
@TableName("em_maintenance_item")
public class MaintenanceItem extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "maintenance_main_id")
    private Long maintenanceMainId;


    /**
     * 保养项
     */
    @TableField(value = "maintenance_item")
    private String maintenanceItem;

    /**
     * 保养项判断标准
     */
    @TableField(value = "maintenance_item_standard")
    private String maintenanceItemStandard;

    /**
     * 保养周期（月）
     */
    @TableField(value = "maintenance_period")
    private Long maintenancePeriod;

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

}
