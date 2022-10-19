package com.aacoptics.notification.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UmsContentFeishuMsgHistoryQueryParam extends BaseParam<UmsContentFeishuMsgHistory> {
    private String batchId;
    private String conType;
}
