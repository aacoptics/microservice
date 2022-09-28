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


    /**
     * 故障描述
     */
    private String faultDesc;


    /**
     * 故障照片
     */
    private String faultPhoto;


    /**
     * 维修描述
     */
    private String repairDesc;



    /**
     * 工单来源
     */
    private String sourceType;

}
