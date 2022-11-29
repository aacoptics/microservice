package com.aacoptics.okr.core.entity.po;

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
@TableName("t_okr_key_result_detail")
public class KeyResultDetail implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectiveId;
    private String keyResultName;
    private String remark;
    private Integer statusInfo;
    private float score;
    @TableLogic
    private String deleted = "N";
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String atUsers;

    @TableField(exist = false)
    private List<FeishuUser> users;

    @TableField(exist = false)
    private List<ActionDetail> actionDetails;

    @TableField(exist = false)
    private Boolean alreadyAlign;
}
