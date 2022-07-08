package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionSummaryQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiModel
@Data
public class ProductionSummaryQueryForm extends BaseQueryForm<ProductionSummaryQueryParam> {

    @ApiModelProperty(value = "项目")
    private String projectName;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询需求开始日期")
    private LocalDate productionDateStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询需求结束日期")
    private LocalDate productionDateEnd;
}
