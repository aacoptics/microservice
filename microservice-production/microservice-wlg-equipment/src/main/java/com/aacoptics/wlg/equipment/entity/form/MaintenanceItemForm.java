package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class MaintenanceItemForm extends BaseForm<MaintenanceItem> {

    @NotNull(message = "保养设备表ID不能为空")
    @ApiModelProperty(value = "保养设备表ID")
    private Long maintenanceMainId;

    @NotBlank(message = "保养项不能为空")
    @ApiModelProperty(value = "保养项")
    private String maintenanceItem;

    @NotBlank(message = "保养项目判断标准不能为空")
    @ApiModelProperty(value = "保养项目判断标准")
    private String maintenanceItemStandard;

    @NotNull(message = "保养周期（月）")
    @ApiModelProperty(value = "保养周期（月）")
    private Long maintenancePeriod;

    @NotNull(message = "起始范围值不能为空")
    @ApiModelProperty(value = "起始范围值")
    private BigDecimal minValue;

    @NotNull(message = "截止范围值不能为空")
    @ApiModelProperty(value = "截止范围值")
    private BigDecimal maxValue;

}
