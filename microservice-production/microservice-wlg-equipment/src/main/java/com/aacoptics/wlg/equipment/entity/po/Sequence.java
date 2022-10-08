package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_sequence")
public class Sequence extends BasePo {


    /**
     * 序列号名称
     */
    @TableField(value = "sequence_name")
    private String sequenceName;


    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 初始值
     */
    @TableField(value = "initial_value")
    private Long initialValue;

    /**
     * 步进值
     */
    @TableField(value = "increment_value")
    private Long incrementValue;

    /**
     * 最大值
     */
    @TableField(value = "max_value")
    private Long maxValue;

    /**
     * 当前值
     */
    @TableField(value = "sequence_number")
    private Long sequenceNumber;



}
