package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.organization.entity.param.DictTypeQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 11:21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DictTypeQueryForm extends BaseQueryForm<DictTypeQueryParam> {

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
