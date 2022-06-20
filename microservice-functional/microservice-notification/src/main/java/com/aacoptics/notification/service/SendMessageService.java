package com.aacoptics.notification.service;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.entity.po.UmsContent;

public interface SendMessageService {

    void sendHandledMessage(NotificationEntity notificationEntity) throws Exception;

    String getMarkDownMessage(UmsContent messageBatch);

    Result sendDingTalkNotification(String jobNumber, String title, String content);
}
