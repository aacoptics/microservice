package com.aacoptics.okr.core.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_okr_process_record")
public class ProcessRecord implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer subjectType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    private String recordDetail;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
