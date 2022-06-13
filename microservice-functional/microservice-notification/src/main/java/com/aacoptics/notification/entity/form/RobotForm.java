package com.aacoptics.notification.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.notification.entity.po.Robot;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@ApiModel
@Data
public class RobotForm extends BaseForm<Robot> {

    @ApiModelProperty(value = "机器人Url")
    @NotBlank(message = "机器人Url不能为空")
    private String robotUrl;

    @ApiModelProperty(value = "机器人名称")
    @NotBlank(message = "机器人名称不能为空")
    private String robotName;

    @ApiModelProperty(value = "机器人类型")
    @NotBlank(message = "机器人类型不能为空")
    private String robotType;

    @ApiModelProperty(value = "机器人状态，1为可用")
    private Boolean status = true;
}