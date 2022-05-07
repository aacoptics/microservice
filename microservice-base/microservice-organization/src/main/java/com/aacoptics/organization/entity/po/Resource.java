package com.aacoptics.organization.entity.po;

import com.aacoptics.common.web.entity.enums.PermissionType;
import com.aacoptics.common.web.entity.po.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource extends BasePo {
    private String code;
    private String type;
    private String url;
    private String method;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PermissionType permissionType;
}
