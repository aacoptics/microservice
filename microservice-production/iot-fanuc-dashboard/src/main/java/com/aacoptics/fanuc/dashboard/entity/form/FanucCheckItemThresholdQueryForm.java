package com.aacoptics.fanuc.dashboard.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckItemThresholdParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class FanucCheckItemThresholdQueryForm extends BaseQueryForm<FanucCheckItemThresholdParam> {

    @ApiModelProperty(value = "ID")
    private Long id;


    @ApiModelProperty(value = "机台")
    private String machineName;


    @ApiModelProperty(value = "项目")
    private String moldFileName;


    @ApiModelProperty(value = "检查项名称")
    private String checkItemName;

}
