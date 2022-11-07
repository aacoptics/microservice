package com.aacoptics.wlg.equipment.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionOrderQueryParam extends BaseParam<InspectionOrder> {


    /**
     * 主键
     */
    private Long id;

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
     * 工单状态
     */
    private String status;

    /**
     * 设备编号
     */
    private String equipNumber;


    /**
     * 点检项
     */
    private String checkItem;
}
