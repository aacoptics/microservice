package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class RepairOrderForm extends BaseForm<RepairOrder> {

    @NotBlank(message = "设备编码不能为空")
    @ApiModelProperty(value = "设备编码")
    private String mchCode;

    @NotBlank(message = "故障描述不能为空")
    @ApiModelProperty(value = "故障描述")
    private String faultDesc;

//    @NotNull(message = "故障照片不能为空")
    @ApiModelProperty(value = "故障照片")
    private Long faultImageId;

    @ApiModelProperty(value = "维修说明")
    private String repairDesc;

    @ApiModelProperty(value = "接单人")
    private String dutyPersonId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "异常分类")
    private String exceptionType;

    @ApiModelProperty(value = "异常子类")
    private String exceptionSubclass;

    @ApiModelProperty(value = "模具")
    private String mould;

    @ApiModelProperty(value = "原因分析")
    private String reason;

    @ApiModelProperty(value = "处理方法")
    private String handleMethod;

    @ApiModelProperty(value = "是否结案")
    private Integer isClosed;


    @ApiModelProperty(value = "长期措施")
    private String longTermMeasure;


}
