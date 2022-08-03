package com.aacoptics.wlg.dashboard.scheduler;

/*@Slf4j
@Component*/
public class DingTalkScheduler {

    /*@Resource
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
    }*/
}