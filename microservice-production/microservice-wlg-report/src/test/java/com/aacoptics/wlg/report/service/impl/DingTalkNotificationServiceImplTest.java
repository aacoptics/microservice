package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.service.DingTalkNotificationService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class DingTalkNotificationServiceImplTest {


    @Autowired
    private DingTalkNotificationService dingTalkNotificationService;

    @Test
    void sendProductionDayDataNotification() {

        try {
            dingTalkNotificationService.sendProductionDayDataNotification("TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    @Test
    void sendWLGProductionDayDataImageNotification() {

        try {
            dingTalkNotificationService.sendWLGProductionDayDataImageNotification("TABLE_TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendGPProductionDayDataImageNotification() {

        try {
            dingTalkNotificationService.sendGPProductionDayDataImageNotification("TABLE_TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}