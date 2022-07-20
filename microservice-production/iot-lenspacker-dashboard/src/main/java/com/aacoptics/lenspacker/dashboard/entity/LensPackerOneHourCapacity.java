package com.aacoptics.lenspacker.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_lenspacker_one_hour_capacity")
public class LensPackerOneHourCapacity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String machineNo;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer output;

    @TableField(exist = false)
    private String machineName;

    @TableField(exist = false)
    private Integer capacity;
}
