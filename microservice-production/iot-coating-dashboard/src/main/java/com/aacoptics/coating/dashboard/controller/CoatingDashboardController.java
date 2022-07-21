package com.aacoptics.coating.dashboard.controller;

import com.aacoptics.coating.dashboard.service.CoatingDashboardService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coatingDashboard")
@Api("coating")
@Slf4j
public class CoatingDashboardController {

    @Autowired
    CoatingDashboardService coatingDashboardService;

    @ApiOperation(value = "查询机台实时状态", notes = "查询机台实时状态")
    @GetMapping("/getStatus")
    public Result getStatus() {
        return Result.success(coatingDashboardService.getCoatingMachineStatus());
    }

    @ApiOperation(value = "查询机台报警状态", notes = "查询机台报警状态")
    @GetMapping("/getAlarmInfo")
    public Result getAlarmInfo() {
        return Result.success(coatingDashboardService.getCoatingMachineAlarmInfo());
    }

    @ApiOperation(value = "查询机台实时状态数量", notes = "查询机台实时状态数量")
    @GetMapping("/getStatusCount")
    public Result getStatusCount() {
        return Result.success(coatingDashboardService.getStatusCount());
    }
}
