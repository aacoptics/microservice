package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class InspectionShiftForm extends BaseForm<InspectionShift> {

    @NotNull(message = "点检设备表ID不能为空")
    @ApiModelProperty(value = "点检设备表ID")
    private Long inspectionMainId;

    @NotBlank(message = "班次不能为空")
    @ApiModelProperty(value = "班次")
    private String shift;

    @NotBlank(message = "班次开始时间不能为空")
    @ApiModelProperty(value = "班次开始时间")
    private String startTime;

    @NotBlank(message = "班次结束时间不能为空")
    @ApiModelProperty(value = "班次结束时间")
    private String endTime;


}
