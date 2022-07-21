package com.aacoptics.general.dashboard.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.general.dashboard.exception.DashboardErrorType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "iot-fanuc-dashboard", fallback = FanucProvider.FanucProviderFallback.class)
public interface FanucProvider {

    @GetMapping(value = "/fanucDashboard/getStatusCount")
    Result getStatusCount();

    @GetMapping(value = "/fanucDashboard/getByFloor")
    Result getAllStatus();

    @Component
    class FanucProviderFallback implements FanucProvider {

        @Override
        public Result getStatusCount() {
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }

        @Override
        public Result getAllStatus() {
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }
    }
}
