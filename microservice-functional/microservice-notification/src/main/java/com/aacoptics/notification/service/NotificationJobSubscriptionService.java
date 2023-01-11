package com.aacoptics.notification.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.NotificationJobSubscription;

public interface NotificationJobSubscriptionService {

    Result add(NotificationJobSubscription notificationJobSubscription);

    boolean delete(Long id);

    boolean update(NotificationJobSubscription notificationJobSubscription);

    boolean updateApproveStatus(String approveId, Integer status);

    boolean removeSubscription(NotificationJobSubscription notificationJobSubscription);

}
