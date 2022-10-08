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
@TableName("em_inspection_shift")
public class InspectionShift extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "inspection_main_id")
    private Long inspectionMainId;

    /**
     * 班次
     */
    @TableField(value = "shift")
    private String shift;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private String endTime;

}
