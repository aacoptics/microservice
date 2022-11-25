package com.aacoptics.okr.core.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TreeModel implements Serializable {

    private String id;

    private String labelName;

    private Integer nodeType;

    private String extendStr;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long alignId;

    private String remark;

    private List<TreeModel> children = new ArrayList<>();
}