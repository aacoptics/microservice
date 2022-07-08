package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel
@Data
public class ProductionSummaryForm extends BaseForm<ProductionSummary> {

    @NotBlank(message = "项目不能为空")
    @ApiModelProperty(value = "项目")
    private String projectName;

    @NotNull(message = "目标生产数量不能为空")
    @ApiModelProperty(value = "目标生产数量")
    private Long targetQty;

    @NotNull(message = "生产月份不能为空")
    @ApiModelProperty(value = "生产月份")
    private LocalDate productionDate;

    @NotNull(message = "实际生产数量不能为空")
    @ApiModelProperty(value = "实际生产数量")
    private Long actualQty;



}
