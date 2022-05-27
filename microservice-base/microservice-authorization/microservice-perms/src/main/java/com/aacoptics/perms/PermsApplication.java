package com.aacoptics.perms;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCreateCacheAnnotation
public class PermsApplication {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(PermsApplication.class, args);
    }
}
