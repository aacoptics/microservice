package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("tb_mold_abnormal_type_config")
public class AbnormalType implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer typeCode;

    private String abnormalType;

    private Boolean enable;

    private LocalDateTime createDateTime;

}