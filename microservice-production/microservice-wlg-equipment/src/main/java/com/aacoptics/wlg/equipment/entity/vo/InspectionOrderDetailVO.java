package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionOrderDetailVO extends BasePo {


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
     * 点检日期
     */
    private LocalDate inspectionDate;

    /**
     * 点检班次
     */
    private String inspectionShift;

    /**
     * 班次开始时间
     */
    private LocalDateTime shiftStartTime;

    /**
     * 班次结束时间
     */
    private LocalDateTime shiftEndTime;

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

    /**
     * 是否返修
     */
    @TableField(value = "is_repair")
    private Integer isRepair;


}
