package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionActualQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiModel
@Data
public class ProductionActualQueryForm extends BaseQueryForm<ProductionActualQueryParam> {

    @ApiModelProperty(value = "项目")
    private String projectName;

    @ApiModelProperty(value = "产品")
    private String product;

    @ApiModelProperty(value = "模具")
    private String mold;

    @ApiModelProperty(value = "周期")
    private String cycle;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询业务发生开始日期")
    private LocalDate actualDateStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询业务发生结束日期")
    private LocalDate actualDateEnd;
}
