package com.aacoptics.organization.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.organization.entity.po.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionQueryParam extends BaseParam<Position> {
    private String name;
}
