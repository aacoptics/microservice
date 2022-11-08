package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.po.FeishuMessage;
import com.aacoptics.wlg.equipment.provider.NotificationProvider;
import com.aacoptics.wlg.equipment.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private NotificationProvider notificationProvider;

    @Override
    public boolean sendInspectionExceptionMessage() {
        FeishuMessage feishuMessage = new FeishuMessage();
        feishuMessage.setSendType("user_id");
        feishuMessage.setSendId("80000061");
        feishuMessage.setContent("test");

        Result result = notificationProvider.sendFeishuNotification(feishuMessage);
        if(result.isSuccess()){
            log.info("success");
        }
        else{
            log.error("fail");
        }
        return false;
    }
}
