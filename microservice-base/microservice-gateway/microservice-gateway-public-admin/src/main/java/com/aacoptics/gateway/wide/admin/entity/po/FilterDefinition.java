package com.aacoptics.gateway.wide.admin.entity.po;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterDefinition {
    private String name;
    private Map<String, String> args = new LinkedHashMap<>();
}
