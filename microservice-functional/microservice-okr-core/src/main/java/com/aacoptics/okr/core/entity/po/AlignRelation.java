package com.aacoptics.okr.core.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_okr_align_relation")
public class AlignRelation implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Integer alignType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long alignId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectiveId;

    private String ownerRealName;

    private String ownerUsername;

    private String objectiveRealName;

    private String objectiveUsername;

    private LocalDateTime createdTime;

    @TableField(exist = false)
    private Integer countNum;
}
