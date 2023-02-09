package com.aacoptics.fanuc.dashboard.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class FanucCheckItemThresholdForm extends BaseForm<FanucCheckItemThreshold> {

    @NotBlank(message = "机台不能为空")
    @ApiModelProperty(value = "机台")
    private String machineName;

    @ApiModelProperty(value = "项目")
    private String moldFileName;

    @NotBlank(message = "检查项不能为空")
    @ApiModelProperty(value = "检查项")
    private String checkItem;

    @NotBlank(message = "检查项名称不能为空")
    @ApiModelProperty(value = "检查项名称")
    private String checkItemName;


    @NotBlank(message = "偏移量不能为空")
    @ApiModelProperty(value = "偏移量名称")
    private Double offsetValue;

}
