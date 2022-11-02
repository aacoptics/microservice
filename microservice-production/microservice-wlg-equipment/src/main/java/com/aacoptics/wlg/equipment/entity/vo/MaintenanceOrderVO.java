package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceOrderVO extends BasePo {


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
     * 设备编号
     */
    private String equipNumber;

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




}
