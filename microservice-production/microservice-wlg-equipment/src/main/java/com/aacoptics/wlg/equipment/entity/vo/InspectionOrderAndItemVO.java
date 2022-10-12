package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
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
public class InspectionOrderAndItemVO extends BasePo {


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


    private List<InspectionOrderItem> inspectionOrderItemList;


}
