package com.aacoptics.notification.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.notification.entity.param.RobotQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

@ApiModel
@Data
public class RobotQueryForm extends BaseQueryForm<RobotQueryParam> {

    @ApiModelProperty(value = "机器人名称")
    private String robotName;

    @ApiModelProperty(value = "机器人类型")
    private String robotType;

    @ApiModelProperty(value = "群类型")
    private String chatType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询开始时间必须小于当前日期")
    @ApiModelProperty(value = "查询开始时间")
    private Date createdTimeStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询结束时间必须小于当前日期")
    @ApiModelProperty(value = "查询结束时间")
    private Date createdTimeEnd;
}
