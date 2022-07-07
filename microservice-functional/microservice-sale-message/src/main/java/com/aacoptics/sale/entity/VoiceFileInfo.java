package com.aacoptics.sale.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class VoiceFileInfo implements Serializable {

    @ApiModelProperty(value = "消息文字")
    @NotBlank(message = "消息文字不能为空")
    private String message;

    @ApiModelProperty(value = "朗读速度（范围-10 - 10）")
    @Min(value = -10, message = "朗读音量最小值为-10")
    @Max(value = 10, message = "朗读音量最大值为10")
    private int speakRate = 0;

    @ApiModelProperty(value = "朗读音量（范围0 - 100）")
    @Min(value = 0, message = "朗读音量最小值为0")
    @Max(value = 100, message = "朗读音量最大值为100")
    private int speakVolume = 100;

}
