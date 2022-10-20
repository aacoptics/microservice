package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ResearchBudgetQueryForm extends BaseQueryForm<ResearchBudgetQueryParam> {

    @ApiModelProperty(value = "上传日志ID")
    private Long uploadLogId;
}
