package com.aacoptics.notification.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_01_content_sub_daiban")
public class UmsContentSubDaiban implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String batchId;

    private String conType;

    private String conTypeDesc;

    private LocalDate keydate;

    private String taskTitle;

    private String taskNotes;

    private LocalDateTime deadline;

    private String executor;

    private String executorName;

    private String followPeople;

    private String followPeopleName;

    private String linkUrl;

    private String linkUrlName;

    private String isStatus;

    private String feishuTaskId;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;
}