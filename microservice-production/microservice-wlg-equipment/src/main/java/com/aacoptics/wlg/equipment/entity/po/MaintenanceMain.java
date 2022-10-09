package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_maintenance_main")
public class MaintenanceMain extends BasePo {

    /**
     * 资产名称
     */
    @TableField(value = "mch_name")
    private String mchName;

    /**
     * 规格
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 型号
     */
    @TableField(value = "type_version")
    private String typeVersion;

    /**
     * 保养周期
     */
    @TableField(value = "maintenance_period")
    private Long maintenancePeriod;

    /**
     * 周期单位
     */
    @TableField(value = "period_unit")
    private String periodUnit;

    /**
     * 保养项
     */
    @TableField(exist = false)
    private List<MaintenanceItem> maintenanceItemList;


}
