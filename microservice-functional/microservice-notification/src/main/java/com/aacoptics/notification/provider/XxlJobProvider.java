package com.aacoptics.notification.provider;

import com.aacoptics.notification.config.FeignConfig;
import com.aacoptics.notification.entity.XxlJobResult;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "xxl-job-server", url = "${xxl.job.admin.addresses}",
        fallbackFactory = XxlJobProvider.XxlJobProviderFallbackFactory.class,
        configuration = FeignConfig.class)
public interface XxlJobProvider {

    @RequestLine("POST /jobinfo/trigger")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult triggerJob(@Param("id") Integer id,
                            @Param("executorParam") String executorParam,
                            @Param("addressList") String addressList);


    @Component
    class XxlJobProviderFallbackFactory implements FallbackFactory<XxlJobProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public XxlJobProvider create(Throwable throwable) {

            return (id, executorParam, addressList) -> {
                throwable.printStackTrace();
                return new XxlJobResult().setCode(-1).setMsg("系统异常");
            };
        }
    }
}