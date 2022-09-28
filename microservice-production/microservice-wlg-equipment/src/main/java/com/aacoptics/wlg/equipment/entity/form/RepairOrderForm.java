package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class RepairOrderForm extends BaseForm<RepairOrder> {

    @NotBlank(message = "设备编码不能为空")
    @ApiModelProperty(value = "设备编码")
    private String mchCode;

    @NotBlank(message = "故障描述不能为空")
    @ApiModelProperty(value = "故障描述")
    private String faultDesc;

    @NotBlank(message = "故障照片不能为空")
    @ApiModelProperty(value = "故障照片")
    private String faultPhoto;

}
