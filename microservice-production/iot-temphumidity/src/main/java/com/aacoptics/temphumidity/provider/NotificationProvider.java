package com.aacoptics.temphumidity.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.temphumidity.entity.FeishuMessage;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservice-notification",
        fallbackFactory = NotificationProviderFallback.class)
public interface NotificationProvider {

    @PostMapping(value = "/notification/sendFeishuNotification")
    @Headers("Content-Type: application/json")
    Result sendFeishuNotification(@RequestBody FeishuMessage feishuMessage);
}