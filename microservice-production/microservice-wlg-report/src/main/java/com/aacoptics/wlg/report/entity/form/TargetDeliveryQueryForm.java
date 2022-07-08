package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.report.entity.param.TargetDeliveryQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiModel
@Data
public class TargetDeliveryQueryForm extends BaseQueryForm<TargetDeliveryQueryParam> {

    @ApiModelProperty(value = "项目")
    private String projectName;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询业务发生开始日期")
    private LocalDate deliveryDateStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(value = "查询业务发生结束日期")
    private LocalDate deliveryDateEnd;
}
