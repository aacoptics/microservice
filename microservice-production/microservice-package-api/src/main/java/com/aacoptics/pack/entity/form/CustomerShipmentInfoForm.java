package com.aacoptics.pack.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class CustomerShipmentInfoForm extends BaseForm<CustomerShipmentInfo> {

    @NotBlank(message = "客户不能为空")
    @ApiModelProperty(value = "客户")
    private String customer;

    @NotBlank(message = "快递单号不能为空")
    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    @NotBlank(message = "订单号不能为空")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @NotBlank(message = "ASN单不能为空")
    @ApiModelProperty(value = "ASN单")
    private String asnNo;
}