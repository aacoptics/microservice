package com.aacoptics.budget.report.entity.form;

import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.common.web.entity.form.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ProductLinePermissionForm extends BaseForm<ProductLinePermission> {


    @NotNull(message = "事业部")
    @ApiModelProperty(value = "事业部")
    private String businessDivision;


    @NotNull(message = "产品线")
    @ApiModelProperty(value = "产品线")
    private String productLine;


    @NotNull(message = "域账号")
    @ApiModelProperty(value = "域账号")
    private String userCode;

}
