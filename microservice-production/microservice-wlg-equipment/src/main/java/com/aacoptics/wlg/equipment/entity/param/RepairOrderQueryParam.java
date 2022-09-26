package com.aacoptics.wlg.equipment.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairOrderQueryParam extends BaseParam<RepairOrder> {


    /**
     * 主键
     */
    private Long id;


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


}
