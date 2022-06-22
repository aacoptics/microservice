package com.aacoptics.notification.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
public class TriggerJobForm implements Serializable {

    @ApiModelProperty(value = "任务ID")
    @NotNull(message = "任务ID不能为空")
    private Integer jobId;

    @ApiModelProperty(value = "执行参数")
    @NotBlank(message = "执行参数不能为空")
    private String executorParam;

    @ApiModelProperty(value = "执行器地址，多个用逗号分隔")
    private String addressList;
}