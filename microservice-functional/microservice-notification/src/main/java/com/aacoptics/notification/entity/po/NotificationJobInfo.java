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
@TableName("notification_job_info")
public class NotificationJobInfo extends BasePo {

    private Integer xxlJobId;

    private String productLine;

    private String planKey;

    private String jobDesc;

    private String remark;

    private LocalDateTime onlineTime;

    private String responsiblePerson;

    private String responsiblePersonName;

    private String itPerson;

    private Boolean jobStatus;

    private Boolean subscriptionEnabled;

    private String executeTime;

    private String jobEnvironment;

    private String notificationNo;
}
