package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrderVO extends BasePo {


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
    private LocalDate repairDatetime;



    /**
     * 责任人
     */
    private String dutyPersonId;

    /**
     * 状态
     */
    private String status;




}
