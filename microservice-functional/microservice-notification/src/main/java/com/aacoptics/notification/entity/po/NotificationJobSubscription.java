package com.aacoptics.notification.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("notification_job_subscription")
public class NotificationJobSubscription extends BasePo {

    private Long notificationJobId;

    private String subscriptionPerson;

    private String approveId;

    private Integer approveStatus;

    @TableField(exist = false)
    private String approveUser;

    @TableField(exist = false)
    private String notificationDesc;

    @TableField(exist = false)
    private Integer subscriptionStatus;
}
