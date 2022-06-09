package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.NotificationEntity;
import com.aacoptics.notification.entity.UmsContent;

public interface SendMessageService {

    void sendHandledMessage(NotificationEntity notificationEntity) throws Exception;

    String getMarkDownMessage(UmsContent messageBatch);
}
