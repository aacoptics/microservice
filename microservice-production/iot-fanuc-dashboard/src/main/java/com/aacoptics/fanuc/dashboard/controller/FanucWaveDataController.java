package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckDataEveryDayParam;
import com.aacoptics.fanuc.dashboard.entity.param.FanucWaveDataParam;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/fanucWaveData")
@Api("fanucWaveData")
@Slf4j
public class FanucWaveDataController {
    @Resource
    FanucWaveDataService fanucWaveDataService;

    @ApiOperation(value = "获取指定时间的模次号", notes = "获取指定时间的模次号")
    @PostMapping("/getCycleNosByTime")
    public Result getCycleNosByTime(@RequestBody FanucWaveDataParam fanucWaveDataParam) {
        return Result.success(fanucWaveDataService.selectCycleNos(fanucWaveDataParam.getStartTime(), fanucWaveDataParam.getEndTime(), fanucWaveDataParam.getMachineNo()));
    }

    @ApiOperation(value = "获取指定模次的波形", notes = "获取指定模次的波形")
    @PostMapping("/getWaveDataByCycleNo")
    public Result getWaveDataByCycleNo(@RequestBody FanucWaveDataParam fanucWaveDataParam) {
        return Result.success(fanucWaveDataService.getFanucWaveData(fanucWaveDataParam.getCycleNos()));
    }
}