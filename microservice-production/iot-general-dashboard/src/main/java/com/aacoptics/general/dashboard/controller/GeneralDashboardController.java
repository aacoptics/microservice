package com.aacoptics.general.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.general.dashboard.service.GeneralDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generalDashboard")
@Api("general")
@Slf4j
public class GeneralDashboardController {

    @Autowired
    GeneralDashboardService generalDashboardService;

    @ApiOperation(value = "查询机台状态数量", notes = "查询机台状态数量")
    @GetMapping("/getGeneralStatusCount")
    public Result getGeneralStatusCount() {
        return Result.success(generalDashboardService.getGeneralStatusCount());
    }


    @ApiOperation(value = "查询机台报警详细数据", notes = "查询机台报警详细数据")
    @GetMapping("/getGeneralCurrentAlarm")
    public Result getGeneralCurrentAlarm() {
        return Result.success(generalDashboardService.getGeneralCurrentAlarm());
    }
}