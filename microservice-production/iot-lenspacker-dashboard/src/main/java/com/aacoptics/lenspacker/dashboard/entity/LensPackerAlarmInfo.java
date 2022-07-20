package com.aacoptics.lenspacker.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_lenspacker_alarm_data")
public class LensPackerAlarmInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String site;

    private String monitMcName;

    private Integer cavityNums;

    private String alarmCode;

    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private Integer duration;

    @TableField(exist = false)
    private Integer alarmCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime dbCreateTime;
}
