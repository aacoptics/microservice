package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.ExceptionSubClass;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class ExceptionSubClassForm extends BaseForm<ExceptionSubClass> {

    @NotNull(message = "异常分类表ID不能为空")
    @ApiModelProperty(value = "异常分类表ID")
    private Long exceptionTypeId;

    @NotBlank(message = "异常子类不能为空")
    @ApiModelProperty(value = "异常子类")
    private String subClass;


}
