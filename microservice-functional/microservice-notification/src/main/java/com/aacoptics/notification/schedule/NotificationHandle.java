package com.aacoptics.notification.schedule;

import com.aacoptics.notification.entity.NotificationEntity;
import com.aacoptics.notification.service.SendMessageService;
import com.alibaba.fastjson.JSONObject;
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
    public void NotificationHandle() throws Exception {

        String param = XxlJobHelper.getJobParam(); //执行参数
        NotificationEntity jobParam = JSONObject.parseObject(param, NotificationEntity.class);
        sendMessageService.sendHandledMessage(jobParam);
    }
}
