package com.aacoptics.budget.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableTransactionManagement
@EnableFeignClients
public class BudgetReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetReportApplication.class, args);
    }
}
