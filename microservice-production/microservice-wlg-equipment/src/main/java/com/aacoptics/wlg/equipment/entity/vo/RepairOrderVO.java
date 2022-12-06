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
    private String faultImageId;


    /**
     * 维修描述
     */
    private String repairDesc;



    /**
     * 工单来源
     */
    private String sourceType;

    /**
     * 设备编号
     */
    private String equipNumber;


    /**
     * 第一次暂存时间
     */
    private LocalDateTime stageDatetime;


    /**
     * 异常分类
     */
    private String exceptionType;


    /**
     * 异常子类
     */
    private String exceptionSubclass;



    /**
     * 模具
     */
    private String mould;



    /**
     * 原因分析
     */
    private String reason;



    /**
     * 处理方法
     */
    private String handleMethod;


    /**
     * 是否结案
     */
    private Integer isClosed;


    /**
     * 长期措施
     */
    private String longTermMeasure;

}
