package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.service.DingTalkNotificationService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DingTalkNotificationServiceImplTest {


    @Autowired
    private DingTalkNotificationService dingTalkNotificationService;

    @Test
    public void sendProductionDayDataNotification() {

        try {
            dingTalkNotificationService.sendProductionDayDataNotification("TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void sendWLGProductionDayDataImageNotification() {

        try {
            dingTalkNotificationService.sendWLGProductionDayDataImageNotification("TABLE_TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendGPProductionDayDataImageNotification() {

        try {
            dingTalkNotificationService.sendGPProductionDayDataImageNotification("TABLE_TEST");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}