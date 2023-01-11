package com.aacoptics.notification.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("xxl_job_info")
public class XxlJobInfo implements Serializable {

    public XxlJobInfo() {
        this.triggerStatus = 0;
        this.triggerLastTime = 0L;
        this.triggerNextTime = 0L;
    }

    @TableId("id")
    private Integer id;

    // 执行器主键ID
    @TableField("job_group")
    private Integer jobGroup;

    @TableField("job_desc")
    private String jobDesc;

    @TableField("add_time")
    private Date addTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("author")
    private String author;        // 负责人

    @TableField("alarm_email")
    private String alarmEmail;    // 报警邮件

    @TableField("schedule_type")
    private String scheduleType;            // 调度类型

    @TableField("schedule_conf")
    private String scheduleConf;            // 调度配置，值含义取决于调度类型

    @TableField("misfire_strategy")
    private String misfireStrategy;            // 调度过期策略

    @TableField("executor_route_strategy")
    private String executorRouteStrategy;    // 执行器路由策略

    @TableField("executor_handler")
    private String executorHandler;            // 执行器，任务Handler名称

    @TableField("executor_param")
    private String executorParam;            // 执行器，任务参数

    @TableField("executor_block_strategy")
    private String executorBlockStrategy;    // 阻塞处理策略

    @TableField("executor_timeout")
    private Integer executorTimeout;            // 任务执行超时时间，单位秒

    @TableField("executor_fail_retry_count")
    private Integer executorFailRetryCount;        // 失败重试次数

    @TableField("glue_type")
    private String glueType;        // GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum

    @TableField("glue_source")
    private String glueSource;        // GLUE源代码

    @TableField("glue_remark")
    private String glueRemark;        // GLUE备注

    @TableField("glue_updatetime")
    private Date glueUpdatetime;    // GLUE更新时间

    @TableField("child_jobid")
    private String childJobId;        // 子任务ID，多个逗号分隔

    @TableField("trigger_status")
    private Integer triggerStatus;        // 调度状态：0-停止，1-运行

    @TableField("trigger_last_time")
    private Long triggerLastTime;    // 上次调度时间

    @TableField("trigger_next_time")
    private Long triggerNextTime;    // 下次调度时间

    @TableField(exist = false)
    private String planKey;

    @TableField(exist = false)
    private Integer xxlJobId;

    @TableField(exist = false)
    private String productLine;

    @TableField(exist = false)
    private String remark;

    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onlineTime;

    @TableField(exist = false)
    private String responsiblePerson;

    @TableField(exist = false)
    private String responsiblePersonName;

    @TableField(exist = false)
    private String itPerson;

    @TableField(exist = false)
    private Boolean jobStatus;

    @TableField(exist = false)
    private Boolean subscriptionEnabled;

    @TableField(exist = false)
    private String executeTime;

    @TableField(exist = false)
    private Integer subscriptionStatus;

    @TableField(exist = false)
    private Long notificationId;
}
