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
    private String maintenanceItem;

    /**
     * 保养项标准
     */
    private String maintenanceItemStandard;

    /**
     * 最小值
     */
    private BigDecimal minValue;

    /**
     * 最大值
     */
    private BigDecimal maxValue;

    /**
     * 实际值
     */
    private BigDecimal actualValue;

    /**
     * 保养结果
     */
    private String maintenanceResult;


    /**
     * 是否异常
     */
    private Integer isException;

    /**
     * 是否完成
     */
    private Integer isFinish;

    /**
     * 是否故障
     */
    private Integer isFault;


    /**
     * 故障描述
     */
    private String faultDesc;

    /**
     * 故障照片
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long faultImageId;

    /**
     * 是否返修
     */
    private Integer isRepair;



}
