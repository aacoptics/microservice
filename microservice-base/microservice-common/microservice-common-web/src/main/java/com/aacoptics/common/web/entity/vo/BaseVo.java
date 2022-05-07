package com.aacoptics.common.web.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseVo<T extends BasePo> implements Serializable {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
}
