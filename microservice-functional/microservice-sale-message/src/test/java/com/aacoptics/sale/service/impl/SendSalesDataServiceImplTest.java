package com.aacoptics.sale.service.impl;

import com.aacoptics.sale.provider.FeishuApi;
import com.aacoptics.sale.service.SendSalesDataService;

import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SendSalesDataServiceImplTest {

    @Resource
    private SendSalesDataService sendSalesDataService;

    @Test
    public void sendSalesDataNotification() {

        try {
            sendSalesDataService.sendSalesDataNotification();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendSalesDataGroupMessage()
    {
        try {
            sendSalesDataService.sendSalesDataGroupMessage();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}