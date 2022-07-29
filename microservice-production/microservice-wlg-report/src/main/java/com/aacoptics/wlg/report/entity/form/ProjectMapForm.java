package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.report.entity.po.ProjectMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ProjectMapForm extends BaseForm<ProjectMap> {

    @NotBlank(message = "商务项目号不能为空")
    @ApiModelProperty(value = "商务项目号")
    private String businessProject;

    @NotNull(message = "内部项目号不能为空")
    @ApiModelProperty(value = "内部项目号")
    private String internalProject;



}
