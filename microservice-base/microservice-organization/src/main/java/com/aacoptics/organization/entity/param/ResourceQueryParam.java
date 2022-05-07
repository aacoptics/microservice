package com.aacoptics.organization.entity.param;

import com.aacoptics.common.web.entity.enums.PermissionType;
import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.organization.entity.po.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceQueryParam extends BaseParam<Resource> {
    private String name;
    private String code;
    private String type;
    private String url;
    private String method;
    private PermissionType permissionType;
}
