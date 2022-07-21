package com.aacoptics.mold.toollife.provider;

import com.aacoptics.common.core.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
@FeignClient(name = "microservice-notification", fallback = EmailProvider.EmailProviderFallback.class)
public interface EmailProvider {

    @PostMapping(value = "/notification/sendEmail")
    Result sendEmail(@RequestBody Map<String, Object> emailSendForm);


    @Component
    class EmailProviderFallback implements EmailProvider {

        @Override
        public Result sendEmail(Map<String, Object> emailSendForm) {
            return Result.fail();
        }

    }
}
