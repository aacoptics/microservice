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
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_inspection_item")
public class InspectionItem extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "inspection_main_id")
    private Long inspectionMainId;


    /**
     * 点检项
     */
    @TableField(value = "check_item")
    private String checkItem;

    /**
     * 点检项标准
     */
    @TableField(value = "check_item_standard")
    private String checkItemStandard;

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
