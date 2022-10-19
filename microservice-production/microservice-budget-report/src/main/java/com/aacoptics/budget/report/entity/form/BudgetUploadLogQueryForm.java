package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class BudgetUploadLogQueryForm extends BaseQueryForm<BudgetUploadLogQueryParam> {

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "文件名称")
    private String excelName;


}
