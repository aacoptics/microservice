package com.aacoptics.organization.entity.form;

import com.aacoptics.common.web.entity.enums.PermissionType;
import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.organization.entity.param.ResourceQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

@ApiModel
@Data
public class ResourceQueryForm extends BaseQueryForm<ResourceQueryParam> {

    @ApiModelProperty(value = "资源类型")
    private String type;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "资源编码")
    private String code;

    @ApiModelProperty(value = "资源路径")
    private String url;

    @ApiModelProperty(value = "资源方法")
    private String method;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询开始时间必须小于当前日期")
    @ApiModelProperty(value = "查询开始时间")
    private Date createdTimeStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询结束时间必须小于当前日期")
    @ApiModelProperty(value = "查询结束时间")
    private Date createdTimeEnd;

    @ApiModelProperty(value = "权限类型")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PermissionType permissionType;
}
