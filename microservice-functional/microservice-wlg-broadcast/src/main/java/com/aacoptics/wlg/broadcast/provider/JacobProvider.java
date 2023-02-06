package com.aacoptics.wlg.broadcast.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.broadcast.entity.form.SpeakerVoiceFileInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "microservice-jacob-provider", fallbackFactory = JacobProvider.JacobProviderFallbackFactory.class)
public interface JacobProvider {


    @PostMapping(value = "/voice/sendToSpeaker")
    Result sendToSpeaker(@RequestBody SpeakerVoiceFileInfo speakerVoiceFileInfo);

    @Component
    class JacobProviderFallbackFactory implements FallbackFactory<JacobProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public JacobProvider create(Throwable throwable) {

            return new JacobProvider() {


                @Override
                public Result sendToSpeaker(SpeakerVoiceFileInfo speakerVoiceFileInfo) {
                    throwable.printStackTrace();
                    return Result.fail(throwable.getLocalizedMessage());
                }
            };

        }
    }
}
