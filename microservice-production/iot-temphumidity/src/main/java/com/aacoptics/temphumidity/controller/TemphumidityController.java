package com.aacoptics.temphumidity.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.temphumidity.service.TemphumidityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temphumidity")
@Api("temphumidity")
@Slf4j
public class TemphumidityController {
    @Autowired
    TemphumidityService temphumidityService;

    @ApiOperation(value = "根据日期查询温湿度信息", notes = "根据日期查询温湿度信息")
    @GetMapping("/getTemphumidityInfoByDate")
    public Result getTemphumidityInfoByDate(@RequestParam String date) {
        return Result.success(temphumidityService.getTemphumidityInfoByDate(date));
    }
}
