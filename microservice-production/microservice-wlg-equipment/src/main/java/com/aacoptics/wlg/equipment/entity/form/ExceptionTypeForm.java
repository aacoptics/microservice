package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.ExceptionType;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class ExceptionTypeForm extends BaseForm<ExceptionType> {

    @NotBlank(message = "异常分类不能为空")
    @ApiModelProperty(value = "异常分类")
    private String exceptionType;


}
