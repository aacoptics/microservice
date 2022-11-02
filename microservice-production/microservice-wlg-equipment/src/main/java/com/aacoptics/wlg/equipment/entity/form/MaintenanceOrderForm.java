package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class MaintenanceOrderForm extends BaseForm<MaintenanceOrder> {

    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "型号")
    private String typeVersion;

    @ApiModelProperty(value = "保养周期")
    private Long maintenancePeriod;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "接单人")
    private String dutyPersonId;

    private List<MaintenanceOrderItem> maintenanceOrderItemList;
}
