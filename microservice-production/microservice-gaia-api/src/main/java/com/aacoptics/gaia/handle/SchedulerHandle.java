package com.aacoptics.gaia.handle;

import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.service.impl.PlanActualPerPersonService;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SchedulerHandle {

    @Resource
    PlanActualPerPersonService planActualPerPersonService;

    @XxlJob("planActualHandle")
    public void planActualHandle() {
        try {
            planActualPerPersonService.sendDingTalkMessage();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}