package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.organization.entity.po.DictType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 11:20
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DictTypeForm extends BaseForm<DictType> {

    @NotBlank(message = "字典名称不能为空")
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @NotBlank(message = "状态不能为空")
    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
