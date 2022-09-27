package com.aacoptics.notification.provider;

import com.aacoptics.notification.config.FeignConfig;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.po.XxlJobResult;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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

    @RequestLine("POST /jobinfo/add")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult add(@RequestBody XxlJobInfo xxlJobInfo);

    @RequestLine("POST /jobinfo/update")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult update(@RequestBody XxlJobInfo xxlJobInfo);

    @RequestLine("POST /jobinfo/remove")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult remove(@Param("id") Integer id);

    @RequestLine("POST /jobinfo/stop")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult stop(@Param("id") Integer id);

    @RequestLine("POST /jobinfo/start")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    XxlJobResult start(@Param("id") Integer id);


    @Component
    class XxlJobProviderFallbackFactory implements FallbackFactory<XxlJobProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public XxlJobProvider create(Throwable throwable) {
            return new XxlJobProvider() {
                @Override
                public XxlJobResult triggerJob(Integer id, String executorParam, String addressList) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }

                @Override
                public XxlJobResult add(XxlJobInfo xxlJobInfo) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }

                @Override
                public XxlJobResult update(XxlJobInfo xxlJobInfo) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }

                @Override
                public XxlJobResult remove(Integer id) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }

                @Override
                public XxlJobResult stop(Integer id) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }

                @Override
                public XxlJobResult start(Integer id) {
                    throwable.printStackTrace();
                    return new XxlJobResult().setCode(-1).setMsg("系统异常");
                }
            };
        }
    }
}