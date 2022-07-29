package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.report.entity.param.ProjectMapQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProjectMapQueryForm extends BaseQueryForm<ProjectMapQueryParam> {

    @ApiModelProperty(value = "商务项目号")
    private String businessProject;


    @ApiModelProperty(value = "内部项目号")
    private String internalProject;
}
