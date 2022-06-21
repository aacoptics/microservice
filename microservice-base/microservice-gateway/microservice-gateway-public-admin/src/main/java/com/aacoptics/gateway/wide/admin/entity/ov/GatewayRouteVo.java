package com.aacoptics.gateway.wide.admin.entity.ov;

import com.aacoptics.common.web.entity.vo.BaseVo;
import com.aacoptics.gateway.wide.admin.entity.po.FilterDefinition;
import com.aacoptics.gateway.wide.admin.entity.po.GatewayRoute;
import com.aacoptics.gateway.wide.admin.entity.po.PredicateDefinition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class GatewayRouteVo extends BaseVo {
    private Long id;
    private String routeId;
    private String description;
    private String status;
    private String uri;
    private Integer orders;
    private String createdBy;
    private LocalDateTime createdTime;
    private String updatedBy;
    private LocalDateTime updatedTime;
    private List<FilterDefinition> filters = new ArrayList<>();
    private List<PredicateDefinition> predicates = new ArrayList<>();

    public GatewayRouteVo(GatewayRoute gatewayRoute) {
        this.id = gatewayRoute.getId();
        this.routeId = gatewayRoute.getRouteId();
        this.uri = gatewayRoute.getUri();
        this.description = gatewayRoute.getDescription();
        this.status = gatewayRoute.getStatus();
        this.orders = gatewayRoute.getOrders();
        this.createdBy = gatewayRoute.getCreatedBy();
        this.createdTime = gatewayRoute.getCreatedTime();
        this.updatedBy = gatewayRoute.getUpdatedBy();
        this.updatedTime = gatewayRoute.getUpdatedTime();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.filters = objectMapper.readValue(gatewayRoute.getFilters(), new TypeReference<List<FilterDefinition>>() {
            });
            this.predicates = objectMapper.readValue(gatewayRoute.getPredicates(), new TypeReference<List<PredicateDefinition>>() {
            });
        } catch (IOException e) {
            log.error("网关路由对象转换失败", e);
        }
    }
}
