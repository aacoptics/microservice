package com.aacoptics.general.dashboard.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.general.dashboard.exception.DashboardErrorType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "iot-lenspacker-dashboard", fallback = LensPackerProvider.LensPackerProviderFallback.class)
public interface LensPackerProvider {

    @GetMapping(value = "/lenspackerDashboard/getStatusCount")
    Result getStatusCount();

    @GetMapping(value = "/lenspackerDashboard/getCurrentAlarm")
    Result getCurrentAlarm();

    @Component
    class LensPackerProviderFallback implements LensPackerProvider {

        @Override
        public Result getStatusCount() {
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }

        @Override
        public Result getCurrentAlarm() {
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }
    }
}
