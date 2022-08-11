package com.aacoptics.mobile.attendance.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.mobile.attendance.service.AttendanceService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feishuAttendance")
@Api("feishuAttendance")
@Slf4j
public class AttendanceController {

    @Resource
    AttendanceService attendanceService;

    @ApiOperation(value = "接收飞书打卡信息", notes = "接收飞书打卡信息")
    @ApiImplicitParam(name = "jsonObject", value = "消息JSON", required = true, dataType = "JSONObject")
    @PostMapping(value = "/receiveAttendanceRecord")
    public Result receiveAttendanceRecord(@RequestBody JSONObject jsonObject) {
        try{
            attendanceService.uploadAttendanceInfo(jsonObject.getString("encrypt"));
        }
        catch (Exception err){
            return Result.fail(err);
        }
        return Result.success();
    }
}