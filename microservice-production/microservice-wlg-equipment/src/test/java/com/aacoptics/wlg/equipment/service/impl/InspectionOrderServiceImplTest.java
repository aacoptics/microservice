package com.aacoptics.wlg.equipment.service.impl;

import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class InspectionOrderServiceImplTest {

    @Resource
    InspectionOrderService inspectionOrderService;

    @Test
    void generateInspectionOrder() {
        inspectionOrderService.generateInspectionOrder();
    }
}