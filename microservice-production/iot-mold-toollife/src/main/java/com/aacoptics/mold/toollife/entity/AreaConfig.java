package com.aacoptics.mold.toollife.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class AreaConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> areaInfo;

    private Set<String> areaCode;
}