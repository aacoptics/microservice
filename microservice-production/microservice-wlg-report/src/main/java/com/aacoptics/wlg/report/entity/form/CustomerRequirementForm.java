package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel
@Data
public class CustomerRequirementForm extends BaseForm<CustomerRequirement> {

    @NotBlank(message = "项目不能为空")
    @ApiModelProperty(value = "项目")
    private String projectName;

    @NotNull(message = "需求数量不能为空")
    @ApiModelProperty(value = "需求数量")
    private Long qty;

    @NotNull(message = "需求月份不能为空")
    @ApiModelProperty(value = "需求月份")
    private LocalDate requirementDate;

    @NotNull(message = "达成率不能为空")
    @ApiModelProperty(value = "达成率")
    private Long completionRate;



}
