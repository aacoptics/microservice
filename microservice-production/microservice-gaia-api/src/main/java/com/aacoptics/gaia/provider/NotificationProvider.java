package com.aacoptics.gaia.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.DingTalkMessage;
import feign.Headers;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "microservice-notification",
        fallbackFactory = NotificationProviderFallback.class)
public interface NotificationProvider {

    @PostMapping(value = "/notification/sendDingTalkNotification")
    @Headers("Content-Type: application/json")
    Result sendDingTalkNotification(@RequestBody DingTalkMessage dingTalkMessage);
}