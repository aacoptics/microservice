package com.aacoptics.common.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final String MICROSERVICE_CLIENT_TOKEN = "microservice-client-token";

    @Override
    public void apply(RequestTemplate template) {
        template.header(MICROSERVICE_CLIENT_TOKEN, "fromGateway");
    }
}