package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.param.ProductLineBudgetQueryParam;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProductLineBudgetQueryForm extends BaseQueryForm<ProductLineBudgetQueryParam> {

    @ApiModelProperty(value = "上传日志ID")
    private Long uploadLogId;
}
