package com.aacoptics.notification.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.param.UmsContentFeishuMsgHistoryQueryParam;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

@ApiModel
@Data
public class UmsContentFeishuMsgHistoryQueryForm extends BaseQueryForm<UmsContentFeishuMsgHistoryQueryParam> {

    @ApiModelProperty(value = "消息批次")
    private String batchId;

    @ApiModelProperty(value = "消息类型名称")
    private String conType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询开始时间必须小于当前日期")
    @ApiModelProperty(value = "查询开始时间")
    private Date createdTimeStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "查询结束时间必须小于当前日期")
    @ApiModelProperty(value = "查询结束时间")
    private Date createdTimeEnd;
}
