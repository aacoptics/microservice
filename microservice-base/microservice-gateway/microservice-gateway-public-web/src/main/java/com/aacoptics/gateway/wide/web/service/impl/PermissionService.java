package com.aacoptics.gateway.wide.web.service.impl;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gateway.wide.web.service.IAuthService;
import com.aacoptics.gateway.wide.web.service.IPermissionService;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService implements IPermissionService {

    /**
     * 由authentication-client模块提供签权的feign客户端
     */
    @Autowired
    private IAuthService authService;

    @Override
    @Cached(name = "gateway_auth::", key = "#authentication+#method+#url",
            cacheType = CacheType.REMOTE, expire = 10)
    public Result permission(String authentication, String url, String method) {
        return authService.hasPermission(authentication, url, method);
    }
}