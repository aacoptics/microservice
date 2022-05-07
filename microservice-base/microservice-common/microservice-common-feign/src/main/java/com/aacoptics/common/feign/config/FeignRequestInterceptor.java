package com.aacoptics.common.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final String IOT_CLIENT_TOKEN = "iot-client-token";

    @Override
    public void apply(RequestTemplate template) {
        template.header(IOT_CLIENT_TOKEN, "test");
    }
}