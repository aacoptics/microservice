package com.aacoptics.perms.service.impl;

import com.aacoptics.common.web.entity.ResourceDefinition;
import com.aacoptics.common.web.entity.enums.PermissionType;
import com.aacoptics.perms.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {

    /**
     * 未在资源库中的URL默认标识
     */
    public static final String NONEXISTENT_URL = "NONEXISTENT_URL";

    @Autowired
    private ResourceService resourceService;

    /**
     * @param authRequest 访问的url,method
     * @return 有权限true, 无权限或全局资源中未找到请求url返回否
     */
    @Override
    public boolean decide(HttpServletRequest authRequest) {
        log.debug("正在访问的url是:{}，method:{}", authRequest.getServletPath(), authRequest.getMethod());
        //获取用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取此url，method访问对应的权限资源信息
        ConfigAttribute urlConfigAttribute = resourceService.findConfigAttributesByUrl(authRequest);
        if (NONEXISTENT_URL.equals(urlConfigAttribute.getAttribute()))
            log.debug("url未在资源池中找到，拒绝访问");
        //获取此访问用户所有角色拥有的权限资源
        Set<ResourceDefinition> userResourceDefinitions = findResourcesByUsername(authentication.getName());
        //用户拥有权限资源 与 url要求的资源进行对比
        return isMatch(urlConfigAttribute, userResourceDefinitions);
    }

    @Override
    public boolean ignoreAuthentication(HttpServletRequest authRequest) {
        log.debug("正在访问的url是:{}，method:{}", authRequest.getServletPath(), authRequest.getMethod());
        ConfigAttribute urlConfigAttribute = resourceService.findConfigAttributesByUrl(authRequest);
        if (NONEXISTENT_URL.equals(urlConfigAttribute.getAttribute())) {
            log.debug("url未在资源池中找到，拒绝访问");
            return false;
        }
        ResourceDefinition resourceDefinition = resourceService.queryByCode(urlConfigAttribute.getAttribute());
        if (Objects.isNull(resourceDefinition)) return false;

        return resourceDefinition.getPermissionType() == PermissionType.WHITELIST;
    }

    /**
     * url对应资源与用户拥有资源进行匹配
     *
     * @param urlConfigAttribute
     * @param userResourceDefinitions
     * @return
     */
    public boolean isMatch(ConfigAttribute urlConfigAttribute, Set<ResourceDefinition> userResourceDefinitions) {
        return userResourceDefinitions.stream().anyMatch(resource -> resource.getCode().equals(urlConfigAttribute.getAttribute()));
    }

    /**
     * 根据用户所被授予的角色，查询到用户所拥有的资源
     *
     * @param username
     * @return
     */
    private Set<ResourceDefinition> findResourcesByUsername(String username) {
        //用户被授予的角色资源
        Set<ResourceDefinition> resourceDefinitions = resourceService.queryByUsername(username);
        if (log.isDebugEnabled()) {
            log.debug("用户被授予角色的资源数量是:{}, 资源集合信息为:{}", resourceDefinitions.size(), resourceDefinitions);
        }
        return resourceDefinitions;
    }
}
