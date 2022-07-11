package com.aacoptics.dingtalk.notification.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("tab_01_content")
public class Content implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String tabDate;

    private String tabType;

    private String title;

    private String content;

    /**
     * 工作通知，0-未通知，1-已通知
     */
    private String flag;

    /**
     * 工作代办，0-未发送，1-已发送，2-待删除，3-已删除
     */
    private String dingtalkFlag;

    private String createdBy;

    private LocalDateTime createTime;

    private String modifyBy;

    private LocalDateTime modifyTime;

    /**
     * 工号
     */
    @TableField(exist = false)
    private String dingtalkUserno;

    /**
     * 钉钉ID
     */
    @TableField(exist = false)
    private String userid;


    @TableField(exist = false)
    private String tabUrl;

    /**
     * 域账号
     */
    @TableField(exist = false)
    private String adaccount;
}