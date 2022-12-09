package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class InspectionItemForm extends BaseForm<InspectionItem> {

    @NotNull(message = "点检设备表ID不能为空")
    @ApiModelProperty(value = "点检设备表ID")
    private Long inspectionMainId;

    @NotBlank(message = "点检项不能为空")
    @ApiModelProperty(value = "点检项")
    private String checkItem;

//    @NotBlank(message = "点检项目判断标准不能为空")
    @ApiModelProperty(value = "点检项目判断标准")
    private String checkItemStandard;

    @ApiModelProperty(value = "起始范围值")
    private BigDecimal minValue;

    @ApiModelProperty(value = "截止范围值")
    private BigDecimal maxValue;

    @NotNull(message = "点检项类型不能为空")
    @ApiModelProperty(value = "点检项类型")
    private String itemType;

    @ApiModelProperty(value = "理论值")
    private String theoreticalValue;

}
