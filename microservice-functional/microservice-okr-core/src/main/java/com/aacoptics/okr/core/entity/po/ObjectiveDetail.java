package com.aacoptics.okr.core.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_okr_objective_detail")
public class ObjectiveDetail implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long periodId;
    private String objectiveName;
    private String remark;
    private Integer statusInfo;
    private float score;
    @TableLogic
    private String deleted = "N";
    private String createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @TableField(exist = false)
    private List<KeyResultDetail> keyResultDetails;

    @TableField(exist = false)
    private List<AlignRelation> AlignRelations;

    @TableField(exist = false)
    private Boolean alreadyAlign;
}
