package com.aacoptics.perms.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.common.web.entity.ResourceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class ResourceProviderFallback implements ResourceProvider {
    @Override
    public Result<Set<ResourceDefinition>> resources() {
        log.error("认证服务启动时加载资源异常！未加载到资源");
        return Result.fail();
    }

    @Override
    public Result<Set<ResourceDefinition>> resources(String username) {
        log.error("认证服务查询用户异常！查询用户资源为空！");
        return Result.success(new HashSet<ResourceDefinition>());
    }

    @Override
    public Result<ResourceDefinition> resourceByCode(String code) {
        log.error("认证服务查询异常！查询[" + code + "]资源为空！");
        return Result.success(null);
    }
}
