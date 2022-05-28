package com.aacoptics.gateway.web.filter;

import com.aacoptics.common.core.exception.SystemErrorType;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gateway.web.service.IAuthService;
import com.aacoptics.gateway.web.service.IPermissionService;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求url权限校验
 */
@Configuration
@ComponentScan
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {

    private static final String MICROSERVICE_CLIENT_TOKEN_USER = "microservice-client-token-user";
    private static final String MICROSERVICE_CLIENT_TOKEN = "microservice-client-token";

    /**
     * 由authentication-client模块提供签权的feign客户端
     */
    @Autowired
    private IAuthService authService;

    @Autowired
    private IPermissionService permissionService;

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
     * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String method = request.getMethodValue();
        String url = request.getPath().value();
        log.debug("url:{},method:{},headers:{}", url, method, request.getHeaders());
        //不需要网关签权的url
        if (authService.ignoreAuthentication(url, method)) {
            ServerHttpRequest.Builder builder = request.mutate();
            //TODO 转发的请求都加上服务间认证token
            builder.header(MICROSERVICE_CLIENT_TOKEN, "fromGateWay");
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }

        Result res = permissionService.permission(authentication, url, method);
        //调用签权服务看用户是否有权限，若有权限进入下一个filter
        if (res.isSuccess()) {
            if ((boolean) res.getData()) {
                ServerHttpRequest.Builder builder = request.mutate();
                //TODO 转发的请求都加上服务间认证token
                builder.header(MICROSERVICE_CLIENT_TOKEN, "fromGateWay");
                //将jwt token中的用户信息传给服务
                builder.header(MICROSERVICE_CLIENT_TOKEN_USER, getUserToken(authentication));
                return chain.filter(exchange.mutate().request(builder.build()).build());
            } else {
                res = Result.fail(SystemErrorType.AUTHORIZATION_FAILED);
                return unauthorized(exchange, res, HttpStatus.FORBIDDEN);
            }
        } else {
            return unauthorized(exchange, res, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 提取jwt token中的数据，转为json
     *
     * @param authentication
     * @return
     */
    private String getUserToken(String authentication) {
        String token = "{}";
        try {
            token = new ObjectMapper().writeValueAsString(authService.getJwt(authentication).getBody());
            return token;
        } catch (JsonProcessingException e) {
            log.error("token json error:{}", e.getMessage());
        }
        return token;
    }

//    /**
//     * 网关拒绝，返回401
//     *
//     * @param
//     */
//    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
//        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        DataBuffer buffer = serverWebExchange.getResponse()
//                .bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
//        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
//    }

    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange, Result result, HttpStatus httpStatus) {
        serverWebExchange.getResponse().setStatusCode(httpStatus);
        DataBuffer dataBuffer = serverWebExchange.getResponse().bufferFactory().wrap(JSONObject.toJSONBytes(result));
        return serverWebExchange.getResponse().writeWith(Flux.just(dataBuffer));
    }
}