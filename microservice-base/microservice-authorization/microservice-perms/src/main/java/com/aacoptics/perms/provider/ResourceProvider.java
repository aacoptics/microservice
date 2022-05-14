package com.aacoptics.perms.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.common.web.entity.ResourceDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Component
@FeignClient(name = "microservice-organization", fallback = ResourceProviderFallback.class)
public interface ResourceProvider {

    @GetMapping(value = "/resource/all")
    Result<Set<ResourceDefinition>> resources();

    @GetMapping(value = "/resource/user/{username}")
    Result<Set<ResourceDefinition>> resources(@PathVariable("username") String username);

    @GetMapping(value = "/resource")
    Result<ResourceDefinition> resourceByCode(@RequestParam(value = "code") String code);
}
