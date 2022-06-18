package com.aacoptic.sep.provider;

import com.aacoptic.sep.config.FeignConfig;
import com.aacoptic.sep.entity.SepTokenResult;
import com.aacoptic.sep.entity.form.ChangeForm;
import com.aacoptic.sep.entity.form.LoginForm;
import com.alibaba.fastjson.JSONObject;
import feign.Headers;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(name = "sep-server", url = "https://10.233.65.50:8446",

        configuration = FeignConfig.class)
public interface SepServerProvider {

    @PostMapping("/sepm/api/v1/identity/authenticate")
    @Headers("Content-Type: application/json")
    SepTokenResult getToken(@RequestBody LoginForm loginForm);

    @RequestMapping(value = "/sepm/api/v1/computers", method = RequestMethod.PATCH)
    @Headers("Content-Type: application/json")
    JSONObject changeClientGroup(@RequestBody List<ChangeForm> changeList, @RequestHeader("Authorization") String token);


    @Component
    class SepServerProviderFallbackFactory implements FallbackFactory<SepServerProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public SepServerProvider create(Throwable throwable) {

            return new SepServerProvider() {
                @Override
                public SepTokenResult getToken(@RequestBody LoginForm loginForm) {
                    throwable.printStackTrace();
                    return null;
                }

                @Override
                public JSONObject changeClientGroup(List<ChangeForm> changeList, String token) {
                    throwable.printStackTrace();
                    return null;
                }
            };
        }
    }
}