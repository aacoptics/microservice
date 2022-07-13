package com.aacoptics.bi.sso.service;


public interface BiSsoService {
    /**
     * 重定向登录BI
     *
     */
    String getRedirectBiUrl(String code) throws Exception;
}
