package com.aacoptics.notification.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@ApiModel
@Data
public class NotificationJobInfoForm {

    private NotificationJobInfo notificationJobInfo;

    private XxlJobInfo xxlJobInfo;
}