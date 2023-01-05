package com.aacoptics.temphumidity.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.temphumidity.service.ElectricMeterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/electricMeter")
@Api("electricMeter")
@Slf4j
public class ElectricMeterController {
    @Autowired
    ElectricMeterService electricMeterService;

    @ApiOperation(value = "查询智能电表信息", notes = "查询智能电表信息")
    @GetMapping("/getElectricMeterInfo")
    public Result getElectricMeterInfo(@RequestParam String buildingNo, @RequestParam String floorNo,
                                       @RequestParam String roomNo, @RequestParam String meterNo,
                                       @RequestParam String startDate, @RequestParam String endDate) {
        return Result.success(electricMeterService.getElectricMeterInfo(buildingNo, floorNo, roomNo, meterNo, startDate, endDate));
    }

    @ApiOperation(value = "根据参数类型获取电表信息查询参数列表", notes = "根据参数类型获取电表信息查询参数列表")
    @GetMapping("/getElectricMeterQueryDataList")
    public Result getElectricMeterQueryDataList(@RequestParam String type) {
        return Result.success(electricMeterService.getElectricMeterQueryDataList(type));
    }

    @ApiOperation(value = "根据日期获取电表用电量", notes = "根据日期获取电表用电量")
    @GetMapping("/getPowerQty")
    public Result getPowerQty(@RequestParam String startDate, @RequestParam String endDate) {
        return Result.success(electricMeterService.getPowerQty(startDate, endDate));
    }
}
