package com.aacoptics.wlg.equipment.config;

import feign.Contract;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public Contract feignContract(){
        return new Contract.Default();
    }

    @Bean
    Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converts){
        return new SpringFormEncoder(new SpringEncoder(converts));
    }
}
