package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class MaintenanceOrderQueryForm extends BaseQueryForm<MaintenanceOrderQueryParam> {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "型号")
    private String typeVersion;
}
