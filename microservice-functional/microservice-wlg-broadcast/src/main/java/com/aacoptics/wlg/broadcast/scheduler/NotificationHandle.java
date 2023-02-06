package com.aacoptics.wlg.broadcast.scheduler;

import com.aacoptics.wlg.broadcast.service.BroadcastService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class NotificationHandle {

    @Resource
    BroadcastService broadcastService;

    @XxlJob("broadcastPerformanceDataAvailableHandle")
    public void broadcastPerformanceDataAvailableHandle() {
        String param = XxlJobHelper.getJobParam(); //执行参数
        try {
            broadcastService.broadcastAllSpeaker();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}