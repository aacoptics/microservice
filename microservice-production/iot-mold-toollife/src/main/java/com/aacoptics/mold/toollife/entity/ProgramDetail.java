package com.aacoptics.mold.toollife.entity;

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
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("tb_mold_program_detail")
public class ProgramDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String machineNo;

    private String monitorNo;

    private String programName;

    private Integer statusCode;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createDateTime;

    @TableField(exist = false)
    private String area;

    @TableField(exist = false)
    private Integer totalTime;

    @TableField(exist = false)
    private Integer runCount;
}