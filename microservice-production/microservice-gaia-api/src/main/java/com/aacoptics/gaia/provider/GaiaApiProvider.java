package com.aacoptics.gaia.provider;

import com.alibaba.fastjson.JSONObject;
import feign.Headers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Component
@FeignClient(name = "gaia-server",
        url = "https://aacopticsoa.gaiaworkforce.com",
        fallbackFactory = GaiaApiProviderFallback.class)
public interface GaiaApiProvider {

    @GetMapping(value = "/api/common/accesstoken")
    @Headers("Content-Type: application/json")
    JSONObject getToken(@RequestParam("appId") String appId,
                        @RequestParam("secret") String secret,
                        @RequestParam("companyCode") String companyCode);

    @GetMapping(value = "/api/AAC/getClassCode")
    @Headers("Content-Type: application/json")
    JSONObject getClassInfo(@RequestHeader("accessToken") String token);
}