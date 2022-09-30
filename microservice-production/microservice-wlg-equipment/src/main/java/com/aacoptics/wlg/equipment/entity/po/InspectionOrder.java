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
@TableName("em_inspection_order")
public class InspectionOrder extends BasePo {


    /**
     * 工单号
     */
    @TableField(value = "order_number")
    private String orderNumber;


    /**
     * 资产编码
     */
    @TableField(value = "mch_code")
    private String mchCode;

    /**
     * 点检日期
     */
    @TableField(value = "inspection_date")
    private LocalDate inspectionDate;

    /**
     * 点检班次
     */
    @TableField(value = "inspection_shift")
    private String inspectionShift;

    /**
     * 班次开始时间
     */
    @TableField(value = "shift_start_time")
    private LocalDateTime shiftStartTime;

    /**
     * 班次结束时间
     */
    @TableField(value = "shift_end_time")
    private LocalDateTime shiftEndTime;

    /**
     * 责任人
     */
    @TableField(value = "duty_person_id")
    private String dutyPersonId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 点检项
     */
    @TableField(exist = false)
    private List<InspectionOrderItem> inspectionOrderItemList;


}
