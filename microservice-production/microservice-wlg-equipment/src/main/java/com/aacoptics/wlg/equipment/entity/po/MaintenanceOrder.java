package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("em_maintenance_order")
public class MaintenanceOrder extends BasePo {


    /**
     * 工单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 资产编码
     */
    @TableField(value = "mch_code")
    private String mchCode;

    /**
     * 保养日期
     */
    @TableField(value = "maintenance_date")
    private LocalDate maintenanceDate;

    /**
     * 保养周期
     */
    @TableField(value = "maintenance_period")
    private Long maintenancePeriod;

    /**
     * 保养周期单位
     */
    @TableField(value = "period_unit")
    private String periodUnit;

    /**
     * 责任人
     */
    @TableField(value = "duty_person_id")
    private String dutyPersonId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 保养项
     */
    @TableField(exist = false)
    private List<MaintenanceOrderItem> maintenanceOrderItemList;


}
