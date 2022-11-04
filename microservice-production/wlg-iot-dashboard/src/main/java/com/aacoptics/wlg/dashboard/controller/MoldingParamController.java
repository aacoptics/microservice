package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.MoldingAnalysisDataParam;
import com.aacoptics.wlg.dashboard.entity.MoldingDataParam;
import com.aacoptics.wlg.dashboard.service.MoldingEventDataService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/moldingMachineParam")
@Api("moldingMachineParam")
@Slf4j
public class MoldingParamController {

    @Resource()
    MoldingMachineParamDataService moldingMachineParamDataService;

    @Resource()
    MoldingEventDataService moldingEventDataService;


    @ApiOperation(value = "获取所有机台号", notes = "获取所有机台号")
    @GetMapping("/getMoldingMachineName")
    public Result getMoldingMachineName() {
        return Result.success(moldingMachineParamDataService.getMachineName());
    }


    @ApiOperation(value = "获取这段时间的waferId", notes = "获取这段时间的waferId")
    @GetMapping(value = "/getWaferIds")
    public Result getWaferIds(@RequestParam String machineName,
                              @RequestParam String startTime,
                              @RequestParam String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingMachineParamDataService.getWaferIds(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df)));
    }

    @ApiOperation(value = "获取这段时间的报警事件", notes = "获取这段时间的报警事件")
    @GetMapping(value = "/getMachineEvents")
    public Result getMachineEvents(@RequestParam String machineName,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingEventDataService.getMachineEvents(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df), new Page(current, size)));
    }

    @ApiOperation(value = "获取这段时间的错误事件", notes = "获取这段时间的错误事件")
    @GetMapping(value = "/getMachineErrors")
    public Result getMachineErrors(@RequestParam String machineName,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingEventDataService.getMachineErrors(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df), new Page(current, size)));
    }

    @ApiOperation(value = "获取模造机参数", notes = "获取模造机参数")
    @PostMapping(value = "/getMoldParam")
    public Result getParamData(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamData(moldingDataParam.getMachineName(), moldingDataParam.getParamName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机参数", notes = "获取模造机参数")
    @PostMapping(value = "/getMoldParamArray")
    public Result getMoldParamArray(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamDataArray(moldingDataParam.getMachineName(), moldingDataParam.getParamName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机参数名", notes = "获取模造机参数名")
    @PostMapping(value = "/getMoldParamName")
    public Result getMoldParamName(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamName(moldingDataParam.getMachineName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机统计数据", notes = "获取模造机统计数据")
    @PostMapping(value = "/getAnalysisData")
    public Result getAnalysisData(@RequestBody MoldingAnalysisDataParam moldingAnalysisDataParam) {
        return Result.success(moldingMachineParamDataService.getAnalysisData(moldingAnalysisDataParam.getMachineName(),
                moldingAnalysisDataParam.getParamNames(),
                moldingAnalysisDataParam.getStartTime(),
                moldingAnalysisDataParam.getEndTime()));
    }
}