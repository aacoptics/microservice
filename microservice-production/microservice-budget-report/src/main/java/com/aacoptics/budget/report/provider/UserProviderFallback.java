package com.aacoptics.budget.report.provider;

import com.aacoptics.common.core.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserProviderFallback implements FallbackFactory<UserProvider> {
    @Override
    public UserProvider create(Throwable throwable) {
        return new UserProvider() {
            @Override
            public Result getByUsername(String username) {
                return Result.fail(throwable);
            }
        };
    }
}
