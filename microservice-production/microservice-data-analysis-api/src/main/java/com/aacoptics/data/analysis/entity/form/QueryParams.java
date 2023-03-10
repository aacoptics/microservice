package com.aacoptics.data.analysis.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class QueryParams {
    @ApiModelProperty(value = "当前页")
    private Integer current = 1;
    @ApiModelProperty(value = "分页大小")
    private Integer size = 10;
    @ApiModelProperty(value = "类别")
    private String category;
    @ApiModelProperty(value = "项目")
    private String project;
    @ApiModelProperty(value = "零件名称")
    private String partName;
    @ApiModelProperty(value = "材料")
    private String material;
    @ApiModelProperty(value = "事业部")
    private String department;
    @ApiModelProperty(value = "镜片数")
    private String lensNumber;
    @ApiModelProperty(value = "模具序号")
    private String moldNo;
    @ApiModelProperty(value = "搜索类型")
    private String searchType;
    @ApiModelProperty(value = "起始值")
    private String startValue;
    @ApiModelProperty(value = "结束值")
    private String endValue;
}
