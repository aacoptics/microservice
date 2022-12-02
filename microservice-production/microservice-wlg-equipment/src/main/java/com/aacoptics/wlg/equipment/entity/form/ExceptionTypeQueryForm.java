package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.equipment.entity.param.ExceptionTypeQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ExceptionTypeQueryForm extends BaseQueryForm<ExceptionTypeQueryParam> {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

}
