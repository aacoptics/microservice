package com.aacoptics.notification.service;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.entity.po.UmsContent;
import com.aacoptics.notification.exception.BusinessException;

public interface SendMessageService {

    String GROUP_MESSAGE = "101";
    String PERSONAL_MESSAGE = "102";

    void sendHandledMessage(NotificationEntity notificationEntity) throws BusinessException;

    String getMarkDownMessage(UmsContent messageBatch);

    Result sendDingTalkNotification(String jobNumber, String title, String content);
}
