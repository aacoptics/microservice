package com.aacoptics.gaia.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PlanActualPerPersonServiceTest {
    @Resource
    PlanActualPerPersonService planActualPerPersonService;

    @Test
    public void sendPersonPlanMsg() {
        planActualPerPersonService.getPersonPlanMsg();
    }
}