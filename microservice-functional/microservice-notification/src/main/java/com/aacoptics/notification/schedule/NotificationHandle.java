package com.aacoptics.notification.schedule;

import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.service.SendMessageService;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class NotificationHandle {

    @Resource
    SendMessageService sendMessageService;

    @XxlJob("NotificationHandle")
    public ReturnT<String> NotificationHandle(String param) {
        NotificationEntity jobParam = JSONObject.parseObject(param, NotificationEntity.class);
        try {
            sendMessageService.sendHandledMessage(jobParam);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnT<>(500, e.getMessage());
        }
    }
}
