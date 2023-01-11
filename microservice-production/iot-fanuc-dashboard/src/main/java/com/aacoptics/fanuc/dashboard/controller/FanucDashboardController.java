package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.entity.param.FanucAnalysisDataParam;
import com.aacoptics.fanuc.dashboard.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fanucDashboard")
@Api("fanuc")
@Slf4j
public class FanucDashboardController {

    @Autowired
    FanucDashboardService fanucDashboardService;

    @Autowired
    FanucCondDataService fanucCondDataService;

    @Autowired
    FanucMonitDataService fanucMonitDataService;

    @Autowired
    FanucAlarmDataService fanucAlarmDataService;

    @Resource
    FanucMasterDataService fanucMasterDataService;

    @Autowired
    FanucOneHourShotCountDataService fanucOneHourShotCountDataService;

    @ApiOperation(value = "查询机台详细数据", notes = "查询机台详细数据")
    @ApiImplicitParam(name = "machineName", value = "机台号", required = true, dataType = "String")
    @GetMapping("/getDetailInfo")
    public Result getDetailInfo(@RequestParam String machineName) {
        return Result.success(fanucDashboardService.getDetailInfo(machineName));
    }

    @ApiOperation(value = "按楼层查询机台实时信息", notes = "按楼层查询机台实时信息")
    @ApiImplicitParam(name = "floor", value = "楼层", required = true, dataType = "String")
    @GetMapping("/getByFloor")
    public Result getByFloor() {
        return Result.success(fanucDashboardService.getByFloor());
    }

    @ApiOperation(value = "按时间查询机台Cond数据", notes = "按时间查询机台Cond数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @PostMapping("/getCondData")
    public Result getCondData(@RequestBody Map<String, String> params) {
        return Result.success(fanucCondDataService.getCondDataByTime(params.get("startTime"),
                params.get("endTime"),
                params.get("machineName")));
    }

    @ApiOperation(value = "按时间查询机台Monit数据", notes = "按时间查询机台Monit数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @PostMapping("/getMonitData")
    public Result getMonitData(@RequestBody Map<String, String> params) {
        return Result.success(fanucMonitDataService.getMonitDataByTime(params.get("startTime"),
                params.get("endTime"),
                params.get("machineName")));
    }

    @ApiOperation(value = "按时间查询机台Alarm数据", notes = "按时间查询机台Alarm数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @PostMapping("/getAlarmData")
    public Result getAlarmData(@RequestBody Map<String, String> params) {
        return Result.success(fanucAlarmDataService.getAlarmDataByTime(params.get("startTime"),
                params.get("endTime"),
                params.get("machineName")));
    }

    @ApiOperation(value = "查询机台实时状态数量", notes = "查询机台实时状态数量")
    @GetMapping("/getStatusCount")
    public Result getStatusCount() {
        return Result.success(fanucDashboardService.getStatusCount());
    }

    @ApiOperation(value = "查询机台总共UPH", notes = "查询机台总共UPH")
    @GetMapping("/getTotalUph")
    public Result getTotalUph() {
        return Result.success(fanucOneHourShotCountDataService.getTotalUph());
    }

    @ApiOperation(value = "实时OEE数据", notes = "查询实时OEE数据")
    @GetMapping("/getCurrentOee")
    public Result getCurrentOee() {
        return Result.success(fanucDashboardService.getCurrentOeeData());
    }

    @ApiOperation(value = "实时状态数据", notes = "查询实时状态数据")
    @GetMapping("/getDigitalData")
    public Result getDigitalData() {
        return Result.success(fanucDashboardService.getDigitalData());
    }

    @ApiOperation(value = "获取机台String list", notes = "获取机台String list")
    @GetMapping(value = "/selectEquips")
    public Result selectEquips() {
        return Result.success(fanucMasterDataService.selectEquipNames());
    }

    @ApiOperation(value = "按时间查询机台Cycle数据", notes = "按时间查询机台Cycle数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @PostMapping("/getAllCycleList")
    public Result getAllCycleList(@RequestBody Map<String, LocalDateTime> params) {
        return Result.success(fanucMonitDataService.getAllCycleList(params.get("startTime"),
                params.get("endTime")));
    }

    @ApiOperation(value = "获取注塑机统计数据", notes = "获取注塑机统计数据")
    @PostMapping(value = "/getAnalysisData")
    public Result getAnalysisData(@RequestBody FanucAnalysisDataParam fanucAnalysisDataParam) {
        return Result.success(fanucDashboardService.getAnalysisData(fanucAnalysisDataParam.getMachineName(),
                fanucAnalysisDataParam.getParamNames(),
                fanucAnalysisDataParam.getStartTime(),
                fanucAnalysisDataParam.getEndTime()));
    }

}