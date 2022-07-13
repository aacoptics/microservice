package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.service.FanucDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fanucDigital")
@Api("fanuc")
@Slf4j
public class FanucDigitalController {
    @Autowired
    FanucDashboardService fanucDashboardService;

    @ApiOperation(value = "实时状态数据", notes = "查询实时状态数据")
    @GetMapping("/getDigitalData")
    public Result getDigitalData() {
        return Result.success(fanucDashboardService.getDigitalData());
    }


    @ApiOperation(value = "实时OEE数据", notes = "查询实时OEE数据")
    @GetMapping("/getCurrentOee")
    public Result getCurrentOee() {
        return Result.success(fanucDashboardService.getCurrentOeeData());
    }


}
