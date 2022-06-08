package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.UmsContent;

public interface SendMessageService {

    void sendFeishuMessage(String robotWebhook);

    String getMarkDownMessage(UmsContent messageBatch);
}
