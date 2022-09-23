package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EquipmentQueryForm extends BaseQueryForm<EquipmentQueryParam> {

    @ApiModelProperty(value = "资产编码")
    private String mchCode;

    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "型号")
    private String typeVersion;

    @ApiModelProperty(value = "出厂编码")
    private String factoryNo;

    @ApiModelProperty(value = "位置编码")
    private String locationNo;

    @ApiModelProperty(value = "资产管理员")
    private String assetManagerId;

    @ApiModelProperty(value = "设备管理员")
    private String mchManagerId;

    @ApiModelProperty(value = "责任人")
    private String dutyPersonId;

}
