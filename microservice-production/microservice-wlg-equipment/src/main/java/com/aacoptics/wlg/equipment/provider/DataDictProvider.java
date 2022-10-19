package com.aacoptics.wlg.equipment.provider;

import com.aacoptics.common.core.vo.Result;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "microservice-organization",
        fallbackFactory = DataDictProviderFallback.class)
public interface DataDictProvider {

    @GetMapping(value = "/dictData/type/{dictType}")
    @Headers("Content-Type: application/json")
    Result getDataDictList(@PathVariable(value="dictType") String dictType);
}