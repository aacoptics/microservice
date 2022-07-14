package com.aacoptics.wlg.dashboard.scheduler;

import com.aacoptics.wlg.dashboard.service.InputReportService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class DingTalkScheduler {

    @Resource
    InputReportService inputReportService;

    @Scheduled(cron = "${dingtalk.cronStr.sendWlgMoldingTwoHourReportCron}")
    @SchedulerLock(name = "sendWlgMoldingTwoHourReport", lockAtMostFor = "30m", lockAtLeastFor = "5m")
    public void sendWlgMoldingTwoHourReport() {
        log.info(LocalDateTime.now() + "开始执行推送两小时报表");
        try {
            inputReportService.SendPicNotification();
        } catch (ApiException | IOException e) {
            log.error("推送两小时报表异常", e);
        }
        log.info(LocalDateTime.now() + "完成推送两小时报表");
    }
}