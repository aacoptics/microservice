package com.aacoptics.dingtalk.notification.service;

import com.taobao.api.ApiException;

public interface SendSalesDataService {

    void sendSalesDataNotification() throws ApiException;

    void sendSalesDataGroupMessage() throws ApiException;


    void deleteSalesDataTodoTask() throws ApiException;
}
