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
@TableName("em_inspection_order_item")
public class InspectionOrderItem extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "inspection_order_id")
    private Long inspectionOrderId;


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

    /**
     * 点检项类型
     */
    @TableField(value = "item_type")
    private String itemType;

    /**
     * 理论值
     */
    @TableField(value = "theoretical_value")
    private String theoreticalValue;


    /**
     * 实际值
     */
    @TableField(value = "actual_value")
    private BigDecimal actualValue;

    /**
     * 点检结果
     */
    @TableField(value = "check_result")
    private String checkResult;


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

//    /**
//     * 是否返修
//     */
//    @TableField(value = "is_repair")
//    private Integer isRepair;

}
