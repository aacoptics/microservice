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
@TableName("ums_01_content_feishu_task_info")
public class FeishuTaskInfo extends BasePo {
    private String taskId;
    private String taskSummary;
    private String taskDescription;
    private String collaborators;
    private String followers;
    private LocalDateTime due;
    private String linkUrl;
    private String linkTitle;
    private Integer taskStatus;
}
