package com.aacoptics.gaia.service.impl;

import com.aacoptics.gaia.service.IGaiaClassService;
import com.aacoptics.gaia.service.IPlanActualPerPersonService;
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
    IPlanActualPerPersonService planActualPerPersonService;

    @Resource
    IGaiaClassService gaiaClassService;

    @Test
    public void sendPersonPlanMsg() {
        gaiaClassService.GetClassInfoFromGaia();
        //planActualPerPersonService.sendDingTalkMessage();
    }
}