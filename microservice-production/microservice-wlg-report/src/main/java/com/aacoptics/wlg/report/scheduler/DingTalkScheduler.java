package com.aacoptics.wlg.report.scheduler;

import com.aacoptics.wlg.report.service.DingTalkNotificationService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class DingTalkScheduler {

    @Resource
    DingTalkNotificationService dingTalkNotificationService;

    @Scheduled(cron = "${dingtalk.cronStr.sendProductionDayDataTestNotificationCron}")
    @SchedulerLock(name = "sendProductionDayDataTestNotification", lockAtMostFor = "30m", lockAtLeastFor = "3m")
    public void sendProductionDayDataTestNotification() {
        log.info(LocalDateTime.now() + "开始执行推送每日产出报告到测试群");
        try {
            dingTalkNotificationService.sendProductionDayDataNotification("TEST");
        } catch (ApiException e) {
            log.error("推送每日产出报告异常", e);
        }
        log.info(LocalDateTime.now() + "完成执行推送每日产出报告测试群");
    }

    @Scheduled(cron = "${dingtalk.cronStr.sendProductionDayDataImageTestNotificationCron}")
    @SchedulerLock(name = "sendProductionDayDataImageTestNotification", lockAtMostFor = "30m", lockAtLeastFor = "3m")
    public void sendProductionDayDataImageTestNotification() {
        log.info(LocalDateTime.now() + "开始执行推送每日产出报告到测试群(表格图片格式)");
        try {
            dingTalkNotificationService.sendWLGProductionDayDataImageNotification("TABLE_TEST");
            dingTalkNotificationService.sendGPProductionDayDataImageNotification("TABLE_TEST");
        } catch (ApiException e) {
            log.error("推送每日产出报告异常(表格图片格式)", e);
        }
        log.info(LocalDateTime.now() + "完成执行推送每日产出报告测试群(表格图片格式)");
    }

    @Scheduled(cron = "${dingtalk.cronStr.sendProductionDayDataManageNotificationCron}")
    @SchedulerLock(name = "sendProductionDayDataManageNotification", lockAtMostFor = "30m", lockAtLeastFor = "3m")
    public void sendProductionDayDataNotification() {
        log.info(LocalDateTime.now() + "开始执行推送每日产出报告到管理群");
        try {
            dingTalkNotificationService.sendProductionDayDataNotification("MANAGE");
        } catch (ApiException e) {
            log.error("推送每日产出报告异常", e);
        }
        log.info(LocalDateTime.now() + "完成执行推送每日产出报告管理群");
    }


    @Scheduled(cron = "${dingtalk.cronStr.sendProductionDayDataImageManageNotificationCron}")
    @SchedulerLock(name = "sendProductionDayDataImageManageNotification", lockAtMostFor = "30m", lockAtLeastFor = "3m")
    public void sendProductionDayDataImageManageNotification() {
        log.info(LocalDateTime.now() + "开始执行推送每日产出报告到管理群(表格图片格式)");
        try {
            dingTalkNotificationService.sendWLGProductionDayDataImageNotification("TABLE_MANAGE");
            dingTalkNotificationService.sendGPProductionDayDataImageNotification("TABLE_MANAGE");
        } catch (ApiException e) {
            log.error("开始执行推送每日产出报告到管理群(表格图片格式)", e);
        }
        log.info(LocalDateTime.now() + "开始执行推送每日产出报告到管理群(表格图片格式)");
    }
}