package com.aacoptics.budget.report.provider;

import com.aacoptics.common.core.vo.Result;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "microservice-organization",
        fallbackFactory = UserProviderFallback.class)
public interface UserProvider {

    @GetMapping(value = "/user/byName")
    @Headers("Content-Type: application/json")
    Result getByUsername(@RequestParam(value = "username") String username);
}