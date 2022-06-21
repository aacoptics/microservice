package com.aacoptics.gateway.wide.admin.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.gateway.wide.admin.entity.po.GatewayRoute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayRouteQueryParam extends BaseParam<GatewayRoute> {
    private String uri;
}
