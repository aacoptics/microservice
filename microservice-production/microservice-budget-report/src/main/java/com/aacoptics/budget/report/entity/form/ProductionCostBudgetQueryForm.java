package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.param.ProductionCostBudgetQueryParam;
import com.aacoptics.budget.report.entity.param.ResearchBudgetQueryParam;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ProductionCostBudgetQueryForm extends BaseQueryForm<ProductionCostBudgetQueryParam> {

    @ApiModelProperty(value = "上传日志ID")
    private Long uploadLogId;

    @ApiModelProperty(value = "事业部")
    private String businessDivision;

    @ApiModelProperty(value = "产品线")
    private List<String> productLineList;
}
