package com.aacoptics.mold.toollife.scheduler;

import com.aacoptics.mold.toollife.service.AbnormalToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MoldToolLifeSch {
    @Autowired
    AbnormalToolService abnormalToolService;

    @Scheduled(cron = "${toolLife.scheduler.OneDayCron}")
    public void dingTalkSchedule() {
        try {
            abnormalToolService.saveAbnormalTool();
            log.info("保存异常数据成功！");
        }catch(Exception err){
            log.error("保存异常数据报错" + err.getMessage());
        }
        try {
            abnormalToolService.sendAbnormalEmail();
            log.info("邮件发送成功！");
        }catch(Exception err){
            log.error("邮件发送报错" + err.getMessage());
        }
    }
}
