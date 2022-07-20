package com.aacoptics.lenspacker.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.lenspacker.dashboard.service.LensPackerAlarmInfoService;
import com.aacoptics.lenspacker.dashboard.service.LensPackerDashboardService;
import com.aacoptics.lenspacker.dashboard.service.LensPackerOneHourCapacityService;
import com.aacoptics.lenspacker.dashboard.service.ValueStreamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/lenspackerDashboard")
@Api("lenspacker")
@Slf4j
public class LensPackerDashboardController {

    @Autowired
    ValueStreamService valueStreamService;

    @Autowired
    LensPackerAlarmInfoService lensPackerAlarmInfoService;

    @Autowired
    LensPackerDashboardService lensPackerDashboardService;

    @Autowired
    LensPackerOneHourCapacityService lensPackerOneHourCapacityService;

//    @ApiOperation(value = "查询机台报警详细数据", notes = "查询机台报警详细数据")
//    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
//    @GetMapping("/getAlarmDetail")
//    public Result getMachineAlarmDetail(@RequestParam Map<String, String> params) {
//        return Result.success(valueStreamService.getMachineAlarmDetail(params.get("startTime"), params.get("endTime")));
//    }
//
//    @ApiOperation(value = "查询机台报警次数详细数据", notes = "查询机台报警次数详细数据")
//    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
//    @GetMapping("/getAlarmCount")
//    public Result getMachineAlarmCount(@RequestParam Map<String, String> params) {
//        return Result.success(valueStreamService.getMachineAlarmCount(params.get("startTime"), params.get("endTime")));
//    }

    @ApiOperation(value = "查询机台报警详细数据", notes = "查询机台报警详细数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @GetMapping("/getAlarmDetail")
    public Result getMachineAlarmDetail(@RequestParam Map<String, String> params) {
        return Result.success(lensPackerAlarmInfoService.getMachineAlarmDetail(params.get("startTime"), params.get("endTime")));
    }

    @ApiOperation(value = "查询机台报警次数详细数据", notes = "查询机台报警次数详细数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @GetMapping("/getAlarmCount")
    public Result getMachineAlarmCount(@RequestParam Map<String, String> params) {
        return Result.success(lensPackerAlarmInfoService.getMachineAlarmCount(params.get("startTime"), params.get("endTime")));
    }

    @ApiOperation(value = "查询机台列表数据", notes = "查询机台列表数据")
    @GetMapping("/getMachineList")
    public Result getMachineList() {
        return Result.success(lensPackerAlarmInfoService.getMachineNameList());
    }

    @ApiOperation(value = "查询机台产能数据", notes = "查询机台产能数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @GetMapping("/getMachineCapacity")
    public Result getMachineCapacity(@RequestParam Map<String, String> params) {
        return Result.success(lensPackerOneHourCapacityService.getMachineCapacity(params.get("startTime"), params.get("endTime")));
    }

    @ApiOperation(value = "查询机台实时状态", notes = "查询机台实时状态")
    @GetMapping("/getAllStatus")
    public Result getAllStatus() {
        return Result.success(lensPackerDashboardService.getStatusInfo());
    }

    @ApiOperation(value = "查询机台实时报警", notes = "查询机台实时报警")
    @GetMapping("/getCurrentAlarm")
    public Result getCurrentAlarm() {
        return Result.success(lensPackerDashboardService.getCurrentAlarmInfo());
    }

    @ApiOperation(value = "查询机台状态数量", notes = "查询机台状态数量")
    @GetMapping("/getStatusCount")
    public Result getStatusCount() {
        return Result.success(lensPackerDashboardService.getStatusCount());
    }

    @ApiOperation(value = "查询机台总共UPH", notes = "查询机台总共UPH")
    @GetMapping("/getTotalUph")
    public Result getTotalUph() {
        return Result.success(lensPackerOneHourCapacityService.getTotalUph());
    }
}