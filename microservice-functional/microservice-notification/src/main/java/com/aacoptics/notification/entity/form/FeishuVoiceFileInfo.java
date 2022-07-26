package com.aacoptics.notification.entity.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class FeishuVoiceFileInfo extends VoiceFileInfo {

    @ApiModelProperty(value = "群名")
    @NotBlank(message = "群名不能为空")
    private String groupName;

}
