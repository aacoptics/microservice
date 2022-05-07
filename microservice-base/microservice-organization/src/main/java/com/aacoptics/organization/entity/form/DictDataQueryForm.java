package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.organization.entity.param.DictDataQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/29 16:07
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DictDataQueryForm extends BaseQueryForm<DictDataQueryParam> {

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
