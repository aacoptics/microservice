package com.aacoptics.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Shao Xiang
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MS_00_PLAN")
public class PlanData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long ID;

    private String PLAN_KEY;

    private String PLAN_NAME;

    private String PLAN_CRON;

    private String MSG_TYPE;

    private String MSG_EXPR;

    private String PLAN_MONTH;

    private String PLAN_DAY;

    private String PLAN_HOUR;

    private String PLAN_MINUTE;

    private String PLAN_SECOND;

    @TableField("TIME_PERIOD")
    private Long TIME_PERIOD;

    @TableField("STATUS")
    private Long STATUS;

    private String CREATE_USER;

    private String UPDATE_USER;

    @TableField("CREATE_TIME")
    private LocalDateTime CREATE_TIME;

    @TableField("UPDATE_TIME")
    private LocalDateTime UPDATE_TIME;

}
