package com.aacoptics.organization.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.organization.entity.po.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuQueryParam extends BaseParam<Menu> {
    private String title;
}
