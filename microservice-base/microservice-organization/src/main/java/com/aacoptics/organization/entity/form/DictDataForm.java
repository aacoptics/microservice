package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.organization.entity.po.DictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/29 16:08
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DictDataForm extends BaseForm<DictData> {

    @NotNull(message = "字典排序不能为空")
    @ApiModelProperty(value = "字典排序")
    private Integer dictSort;

    @NotBlank(message = "字典标签不能为空")
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;


    @NotBlank(message = "字典键值不能为空")
    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "样式属性（其他样式扩展）")
    private String cssClass;

    @ApiModelProperty(value = "表格字典样式")
    private String listClass;

    @ApiModelProperty(value = "是否默认（Y是 N否）")
    private String isDefault;

    @NotBlank(message = "状态（0正常 1停用）不能为空")
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
