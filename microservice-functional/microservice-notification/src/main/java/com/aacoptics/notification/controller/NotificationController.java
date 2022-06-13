package com.aacoptics.notification.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.form.XxlJobInfoForm;
import com.aacoptics.notification.service.XxlJobInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Notification")
@Api("Notification")
@Slf4j
public class NotificationController {
    @Resource
    XxlJobInfoService xxlJobInfoService;

    @ApiOperation(value = "保存消息推送计划数据", notes = "保存消息推送计划数据")
    @GetMapping("/addNotificationTask")
    public Result addNotificationTask(@RequestParam XxlJobInfoForm xxlJobInfoForm) {
        XxlJobInfo xxlJobInfo = xxlJobInfoForm.toPo();
        return Result.success(xxlJobInfoService.add(xxlJobInfo));
    }
}
