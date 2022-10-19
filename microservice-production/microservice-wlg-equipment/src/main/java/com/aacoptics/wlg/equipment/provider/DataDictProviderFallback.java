package com.aacoptics.wlg.equipment.provider;

import com.aacoptics.common.core.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataDictProviderFallback implements FallbackFactory<DataDictProvider> {
    @Override
    public DataDictProvider create(Throwable throwable) {
        return new DataDictProvider() {
            @Override
            public Result getDataDictList(String dictType) {
                return Result.fail(throwable);
            }
        };
    }
}
