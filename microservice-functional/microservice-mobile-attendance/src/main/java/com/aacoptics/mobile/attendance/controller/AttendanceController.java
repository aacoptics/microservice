package com.aacoptics.mobile.attendance.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import com.aacoptics.mobile.attendance.entity.vo.AttendanceRecord;
import com.aacoptics.mobile.attendance.service.AttendanceService;
import com.aacoptics.mobile.attendance.util.Decrypt;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feishuAttendance")
@Api("飞书移动考勤")
@Slf4j
public class AttendanceController {

    @Resource
    AttendanceService attendanceService;

    @Resource
    private FeishuAppKeyConfig feishuAppKeyConfig;

    @ApiOperation(value = "接收飞书打卡信息", notes = "接收飞书打卡信息")
    @ApiImplicitParam(name = "jsonObject", value = "消息JSON", required = true)
    @PostMapping(value = "/receiveAttendanceRecord")
    public Result receiveAttendanceRecord(@RequestBody String bodyString,
                                          @RequestHeader("X-Lark-Request-Timestamp") String timeStamp,
                                          @RequestHeader("X-Lark-Request-Nonce") String nonce,
                                          @RequestHeader("X-Lark-Signature") String sign) {
        Decrypt d = new Decrypt(feishuAppKeyConfig.getEncryptKey());
        try {
            String signature = d.calculateSignature(timeStamp, nonce, feishuAppKeyConfig.getEncryptKey(), bodyString);
            if (!signature.equals(sign))
                return Result.fail("签名不一致！");
            JSONObject bodyJson = JSONObject.parseObject(bodyString, JSONObject.class);
            AttendanceRecord attendanceRecord = JSONObject.parseObject(d.decrypt(bodyJson.getString("encrypt")), AttendanceRecord.class);
            if (attendanceService.uploadAttendanceInfo(attendanceRecord)) {
                return Result.success();
            } else return Result.fail();
        } catch (Exception err) {
            return Result.fail(err);
        }
    }
}