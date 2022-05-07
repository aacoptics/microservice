package com.aacoptics.gateway.web.service;

import com.aacoptics.common.core.vo.Result;

public interface IPermissionService {
    /**
     * @param authentication
     * @param method
     * @param url
     * @return
     */
    Result permission(String authentication, String url, String method);
}