package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.aacoptics.okr.core.service.FeishuService;
import com.aacoptics.okr.core.service.PeriodInfoService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/aacOkrFeishu")
@Api("aacOkrFeishu")
@Slf4j
public class OkrFeishuController {
    @Resource
    private FeishuService feishuService;

    @ApiOperation(value = "获取认证信息", notes = "获取认证信息")
    @ApiImplicitParam(name = "authCode", required = true, dataType = "String")
    @GetMapping("/getAuthInfo")
    public Result getAuthInfo(@Valid @RequestParam String authCode) {
        FeishuUser feishuUser = feishuService.getFeishuUserByAuthCode(authCode);
        return feishuUser == null ? Result.fail() : Result.success(feishuUser);
    }
}