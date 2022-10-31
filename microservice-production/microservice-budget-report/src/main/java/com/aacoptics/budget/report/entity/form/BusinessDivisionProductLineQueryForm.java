package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.param.BusinessDivisionProductLineQueryParam;
import com.aacoptics.budget.report.entity.param.ProductLinePermissionQueryParam;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class BusinessDivisionProductLineQueryForm extends BaseQueryForm<BusinessDivisionProductLineQueryParam> {


    @ApiModelProperty(value = "事业部")
    private String businessDivision;

    @ApiModelProperty(value = "产品线")
    private String productLine;

}
