package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class InspectionOrderItemForm extends BaseForm<InspectionOrderItem> {

    @NotNull(message = "点检设备表ID不能为空")
    @ApiModelProperty(value = "点检设备表ID")
    private Long inspectionMainId;

    @NotBlank(message = "点检项不能为空")
    @ApiModelProperty(value = "点检项")
    private String checkItem;

    @NotBlank(message = "点检项目判断标准不能为空")
    @ApiModelProperty(value = "点检项目判断标准")
    private String checkItemStandard;

    @NotNull(message = "起始范围值不能为空")
    @ApiModelProperty(value = "起始范围值")
    private BigDecimal minValue;

    @NotNull(message = "截止范围值不能为空")
    @ApiModelProperty(value = "截止范围值")
    private BigDecimal maxValue;


    @ApiModelProperty(value = "实际值")
    private BigDecimal actualValue;

    @ApiModelProperty(value = "点检结果")
    private String checkResult;


    @ApiModelProperty(value = "是否异常")
    private Integer isException;

    @ApiModelProperty(value = "是否完成")
    private Integer isFinish;

    @ApiModelProperty(value = "是否故障")
    private Integer isFault;


    @ApiModelProperty(value = "故障描述")
    private String faultDesc;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "故障照片")
    private Long faultImageId;

    @ApiModelProperty(value = "是否返修")
    private Integer isRepair;

}
