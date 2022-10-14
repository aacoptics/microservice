package com.aacoptics.notification.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_01_content_feishu_msg_history")
public class UmsContentFeishuMsgHistory extends BasePo {
    private String batchId;
    private String chatId;
    private String messageId;
    private String msgType;
}
