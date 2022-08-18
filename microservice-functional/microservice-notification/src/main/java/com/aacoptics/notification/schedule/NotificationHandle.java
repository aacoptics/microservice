package com.aacoptics.notification.schedule;

import com.aacoptics.notification.entity.vo.NotificationEntity;
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

    @XxlJob("RobotHandle")
    public void RobotHandle() {
        String param = XxlJobHelper.getJobParam(); //执行参数
        XxlJobHelper.log(param);
        NotificationEntity jobParam = JSONObject.parseObject(param, NotificationEntity.class);
        try {
            sendMessageService.sendHandledMessage(jobParam);
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            if (e.getMessage().equals("当前没有需要推送的批次号！"))
                XxlJobHelper.handleSuccess("当前没有需要推送的批次号！");
            else{
                XxlJobHelper.handleFail(e.getMessage());
            }
        }
    }
}