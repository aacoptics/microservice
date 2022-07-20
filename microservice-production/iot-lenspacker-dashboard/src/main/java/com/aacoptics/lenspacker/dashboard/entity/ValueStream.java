package com.aacoptics.lenspacker.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author Yan Shangqi
 * @since 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ValueStream implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "entry_id", type = IdType.AUTO)
    private Integer entryId;

    private String entityId;

    private String sourceId;

    private LocalDateTime time;

    private Integer propertyType;

    private String propertyName;

    private String propertyValue;

    @TableField(exist = false)
    private String machineName;

    @TableField(exist = false)
    private String alarmType;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private String alarmDesc;

    @TableField(exist = false)
    private Integer duration;

    @TableField(exist = false)
    private Integer alarmCount;

    @TableField(exist = false)
    private Integer capacity;
}