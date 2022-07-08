package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.service.DingTalkNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dingTalkMessage")
@Api("dingTalkMessage")
@Slf4j
public class DingTalkMessageController {

    @Autowired
    DingTalkNotificationService dingTalkNotificationService;


    @ApiOperation(value = "推送WLG数据到钉钉测试群（表格模式）", notes = "推送WLG数据到钉钉测试群（表格模式）")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/sendProductionDayDataImageNotification")
    public Result sendProductionDayDataImageNotification() {
        try {
            dingTalkNotificationService.sendWLGProductionDayDataImageNotification("TABLE_TEST");
            dingTalkNotificationService.sendGPProductionDayDataImageNotification("TABLE_TEST");
        } catch (Exception e) {
            log.error("推送失败", e);
            return Result.fail(e.getMessage());
        }
        return Result.success();
    }


}