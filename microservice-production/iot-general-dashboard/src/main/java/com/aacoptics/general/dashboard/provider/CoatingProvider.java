package com.aacoptics.general.dashboard.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.general.dashboard.exception.DashboardErrorType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "iot-coating-dashboard-xny", fallback = CoatingProvider.CoatingProviderFallback.class)
public interface CoatingProvider {

    @GetMapping(value = "/coatingDashboard/getStatusCount")
    Result getStatusCount();

    @GetMapping(value = "/coatingDashboard/getAlarmInfo")
    Result getAlarmInfo();


    @Component
    class CoatingProviderFallback implements CoatingProvider {

        @Override
        public Result getStatusCount() {
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }

        @Override
        public Result getAlarmInfo(){
            return Result.fail(DashboardErrorType.INNER_ERROR);
        }
    }
}
