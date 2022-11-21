package com.aacoptics.hikvision.api.scheduler;

import com.aacoptics.hikvision.api.service.HikvisionDoorEventRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class HikvisionJobHandle {

    @Resource
    HikvisionDoorEventRecordService hikvisionDoorEventRecordService;

    @XxlJob("HikvisionDoorRecordHandle")
    public void HikvisionDoorRecordHandle() {
        try {
            hikvisionDoorEventRecordService.getHikvisionDoorEventRecord();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}