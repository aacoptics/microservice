package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckDataEveryDayParam;
import com.aacoptics.fanuc.dashboard.service.FanucCheckDataEveryDayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fanucCheckData")
@Api("fanucCheckData")
@Slf4j
public class FanucCheckDataEveryDayController {

    @Autowired
    FanucCheckDataEveryDayService fanucCheckDataEveryDayService;


    @ApiOperation(value = "获取所有机台", notes = "获取所有机台")
    @GetMapping("/getAllMachineName")
    public Result getAllMachineName() {
        return Result.success(fanucCheckDataEveryDayService.getAllMachineName());
    }

    @ApiOperation(value = "获取所有项目", notes = "获取所有项目")
    @GetMapping("/getAllMoldFileName")
    public Result getAllMoldFileName(@RequestBody Map<String, String> params) {
        return Result.success(fanucCheckDataEveryDayService.getAllMoldFileName());
    }

    @ApiOperation(value = "获取注塑机每日点检数据", notes = "获取注塑机每日点检数据")
    @PostMapping(value = "/getFanucCheckDataEveryDay")
    public Result getFanucCheckDataEveryDay(@RequestBody FanucCheckDataEveryDayParam fanucCheckDataEveryDayParam) {
        return Result.success(fanucCheckDataEveryDayService.getFanucCheckDataEveryDay(fanucCheckDataEveryDayParam.getMachineNames(),
                fanucCheckDataEveryDayParam.getMoldFileNames(),
                fanucCheckDataEveryDayParam.getStartTime(),
                fanucCheckDataEveryDayParam.getEndTime()));
    }

}