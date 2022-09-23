package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel
@Data
public class InspectionMainForm extends BaseForm<InspectionMain> {

    @NotBlank(message = "资产名称不能为空")
    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @NotBlank(message = "规格不能为空")
    @ApiModelProperty(value = "规格")
    private String spec;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号")
    private String typeVersion;

}
