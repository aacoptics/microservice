package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RepairOrderQueryForm extends BaseQueryForm<RepairOrderQueryParam> {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "资产编码")
    private String mchCode;

    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "型号")
    private String typeVersion;

    @ApiModelProperty(value = "工单状态")
    private String status;

    @ApiModelProperty(value = "设备编号")
    private String equipNumber;
}
