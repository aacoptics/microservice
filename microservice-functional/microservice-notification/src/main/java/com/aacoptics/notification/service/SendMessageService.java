package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.entity.po.UmsContent;

public interface SendMessageService {

    void sendHandledMessage(NotificationEntity notificationEntity) throws Exception;

    String getMarkDownMessage(UmsContent messageBatch);
}
