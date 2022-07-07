package com.aacoptics.sale.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.sale.entity.FeishuVoiceFileInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Component
@FeignClient(name = "microservice-jacob-provider", fallbackFactory = JacobProvider.JacobProviderFallbackFactory.class)
public interface JacobProvider {

    @PostMapping(value = "/voice/sendFeishuVoice")
    Result sendFeishuVoiceMsg(@RequestBody FeishuVoiceFileInfo feishuVoiceFileInfo);

    @Component
    class JacobProviderFallbackFactory implements FallbackFactory<JacobProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public JacobProvider create(Throwable throwable) {

            return new JacobProvider() {

                @Override
                public Result sendFeishuVoiceMsg(FeishuVoiceFileInfo feishuVoiceFileInfo) {
                    throwable.printStackTrace();
                    return Result.fail(throwable.getLocalizedMessage());
                }
            };

        }
    }
}