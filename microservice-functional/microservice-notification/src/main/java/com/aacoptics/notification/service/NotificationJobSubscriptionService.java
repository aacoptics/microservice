package com.aacoptics.notification.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.NotificationJobSubscription;

import java.util.List;

public interface NotificationJobSubscriptionService {

    Result add(NotificationJobSubscription notificationJobSubscription);

    boolean delete(Long id);

    boolean update(NotificationJobSubscription notificationJobSubscription);

    boolean updateApproveStatus(String approveId, Integer status);

    List<String> listSubscriptionUsers(String planKey);

    boolean removeSubscription(NotificationJobSubscription notificationJobSubscription);

}
