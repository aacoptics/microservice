package com.aacoptics.mobile.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableScheduling
@EnableFeignClients
public class MobileAttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MobileAttendanceApplication.class, args);
    }
}
