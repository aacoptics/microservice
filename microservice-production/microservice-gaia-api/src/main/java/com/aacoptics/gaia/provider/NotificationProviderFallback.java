package com.aacoptics.gaia.provider;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.DingTalkMessage;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationProviderFallback implements FallbackFactory<NotificationProvider> {
    @Override
    public NotificationProvider create(Throwable throwable) {
        return new NotificationProvider() {
            @Override
            public Result sendDingTalkNotification(DingTalkMessage dingTalkMessage) {
                return Result.fail(throwable);
            }
        };
    }
}
