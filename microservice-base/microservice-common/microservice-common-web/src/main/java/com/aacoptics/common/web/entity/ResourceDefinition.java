package com.aacoptics.common.web.entity;

import com.aacoptics.common.web.entity.enums.EditType;
import com.aacoptics.common.web.entity.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDefinition implements Serializable {
    private String code;
    private String name;
    private String type;
    private String url;
    private String method;
    private String description;
    private PermissionType permissionType;
    private EditType editType = EditType.ADD;
}
