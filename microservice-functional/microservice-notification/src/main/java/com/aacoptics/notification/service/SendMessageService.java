package com.aacoptics.notification.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.vo.FeishuMessage;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.entity.po.UmsContent;
import com.aacoptics.notification.exception.BusinessException;
import org.springframework.scheduling.annotation.Async;

public interface SendMessageService {

    String GROUP_MESSAGE = "101";
    String PERSONAL_MESSAGE = "102";

    String TASK_MESSAGE = "103";

    void sendHandledMessage(NotificationEntity notificationEntity) throws BusinessException;

    @Async
    void sendSubscriptionNotification(NotificationEntity notificationEntity, UmsContent messageBatch, String fileKey, JSONObject cardJson);

    String getMarkDownMessage(UmsContent messageBatch);

    Result sendFeishuNotification(FeishuMessage feishuMessage);
}
