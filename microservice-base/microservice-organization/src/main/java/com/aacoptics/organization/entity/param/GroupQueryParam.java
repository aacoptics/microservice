package com.aacoptics.organization.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.organization.entity.po.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupQueryParam extends BaseParam<Group> {
    private String name;
}
