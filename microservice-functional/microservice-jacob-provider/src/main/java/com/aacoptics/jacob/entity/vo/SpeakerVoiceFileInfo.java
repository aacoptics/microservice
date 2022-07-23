package com.aacoptics.jacob.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SpeakerVoiceFileInfo extends VoiceFileInfo {

    @ApiModelProperty(value = "扬声器SN")
    @NotBlank(message = "扬声器SN不能为空")
    private String speakerSn;

    @ApiModelProperty(value = "扬声器Ip")
    @NotBlank(message = "扬声器Ip不能为空")
    private String speakerIp;

    @ApiModelProperty(value = "扬声器Port")
    @NotBlank(message = "扬声器Port不能为空")
    private Integer speakerPort;

}
