package com.aacoptics.common.web.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode()
@ApiModel
@Accessors(chain = true)
@Data
public class XiaomiApiForm {

    @NotBlank(message = "接口编号不能为空")
    @ApiModelProperty(value = "接口编号")
    private String apiId;

    @ApiModelProperty(value = "接口传参，为Object")
    private Object param;
}
