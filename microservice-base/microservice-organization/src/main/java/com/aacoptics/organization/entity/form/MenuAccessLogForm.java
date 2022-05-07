package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.organization.entity.po.MenuAccessLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class MenuAccessLogForm extends BaseForm<MenuAccessLog> {

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称")
    private String name;

}
