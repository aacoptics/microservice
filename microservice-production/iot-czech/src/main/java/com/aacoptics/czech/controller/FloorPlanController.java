package com.aacoptics.czech.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.czech.entity.MachineRemark;
import com.aacoptics.czech.service.FloorPlanMachineInfoService;
import com.aacoptics.czech.service.MachineRemarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/floorPlan")
@Api("czechFloorPlan")
@Slf4j
public class FloorPlanController {
    @Autowired
    FloorPlanMachineInfoService floorPlanMachineInfoService;

    @Autowired
    MachineRemarkService machineRemarkService;

    @ApiOperation(value = "根据楼层查询机台信息", notes = "根据楼层查询机台信息")
    @GetMapping("/getMachineInfoByFloor")
    public Result getMachineInfoByFloor(@RequestParam int startNumber, @RequestParam int endNumber) {
        return Result.success(floorPlanMachineInfoService.getMachineInfoByFloor(startNumber, endNumber));
    }

    @ApiOperation(value = "查询所有机台信息", notes = "查询所有机台信息")
    @GetMapping("/getAllMachineInfo")
    public Result getAllMachineInfo() {
        return Result.success(floorPlanMachineInfoService.getAllMachineInfo());
    }

    @ApiOperation(value = "根据楼层号查询机台信息", notes = "根据楼层号查询机台信息")
    @GetMapping("/getMachineInfoByFloorNumber")
    public Result getMachineInfoByFloorNumber(@RequestParam int floorNumber) {
        return Result.success(floorPlanMachineInfoService.getMachineInfoByFloorNumber(floorNumber));
    }

    @ApiOperation(value = "根据机台号查询机台信息", notes = "根据机台号查询机台信息")
    @GetMapping("/getMachineInfoByMachineNumber")
    public Result getMachineInfoByMachineNumber(@RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getMachineInfoByMachineNumber(machineNumber));
    }

    @ApiOperation(value = "获取Spindle T温度数据", notes = "获取Spindle T温度数据")
    @GetMapping("/getSpindleTemperature")
    public Result getSpindleTemperature(@RequestParam String startTime, @RequestParam String endTime, @RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getSpindleTemperature(startTime, endTime, machineNumber));
    }

    @ApiOperation(value = "获取Air T温度数据", notes = "获取Air T温度数据")
    @GetMapping("/getAirTemperature")
    public Result getAirTemperature(@RequestParam String startTime, @RequestParam String endTime, @RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getAirTemperature(startTime, endTime, machineNumber));
    }

    @ApiOperation(value = "获取Bearing T温度数据", notes = "获取Bearing T温度数据")
    @GetMapping("/getBearingTemperature")
    public Result getBearingTemperature(@RequestParam String startTime, @RequestParam String endTime, @RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getBearingTemperature(startTime, endTime, machineNumber));
    }

    @ApiOperation(value = "获取Motor T温度数据", notes = "获取Motor T温度数据")
    @GetMapping("/getMotorTemperature")
    public Result getMotorTemperature(@RequestParam String startTime, @RequestParam String endTime, @RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getMotorTemperature(startTime, endTime, machineNumber));
    }

    @ApiOperation(value = "根据机台号获取状态信息", notes = "根据机台号获取状态信息")
    @GetMapping("/getStatusInfoByMachineNumber")
    public Result getStatusInfoByMachineNumber(@RequestParam int machineNumber) {
        return Result.success(floorPlanMachineInfoService.getStatusInfoByMachineNumber(machineNumber));
    }

    @ApiOperation(value = "保存备注信息", notes = "保存备注信息")
    @GetMapping("/saveRemark")
    public Result saveRemark(@RequestParam String machineNumber, @RequestParam String content) {
        return Result.success(machineRemarkService.saveRemark(machineNumber, content));
    }

    @ApiOperation(value = "修改备注信息", notes = "修改备注信息")
    @PostMapping("/updateRemark")
    public Result updateRemark(@RequestBody MachineRemark machineRemark, @RequestParam String newContent) {
        machineRemarkService.updateRemark(machineRemark, newContent);
        return Result.success();
    }

    @ApiOperation(value = "删除备注信息", notes = "删除备注信息")
    @PostMapping("/deleteRemark")
    public Result deleteRemark(@RequestBody MachineRemark machineRemark) {
        machineRemarkService.deleteRemark(machineRemark);
        return Result.success();
    }

    @ApiOperation(value = "根据机台号查询备注信息", notes = "根据机台号查询备注信息")
    @GetMapping("/getRemarkByMachineNumber")
    public Result getRemarkByMachineNumber(@RequestParam String machineNumber) {
        return Result.success(machineRemarkService.getRemarkByMachineNumber(machineNumber));
    }
}
