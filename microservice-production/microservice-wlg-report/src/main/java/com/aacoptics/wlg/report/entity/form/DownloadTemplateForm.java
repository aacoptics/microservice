package com.aacoptics.wlg.report.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.common.web.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class DownloadTemplateForm extends BaseForm<BasePo> {

    @ApiModelProperty(value = "模板类型")
    private String type;


}
