package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.service.FeishuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "搜索飞书人员", notes = "搜索飞书人员")
    @ApiImplicitParam(name = "userInfo", required = true, dataType = "String")
    @GetMapping("/getFeishuUsers")
    public Result getFeishuUsers(@Valid @RequestParam String userInfo) {
        return Result.success(feishuService.getFeishuUsers(userInfo));
    }

    @ApiOperation(value = "搜索飞书人员", notes = "搜索飞书人员")
    @ApiImplicitParam(name = "employeeNo", required = true, dataType = "String")
    @GetMapping("/getFeishuUser")
    public Result getFeishuUser(@Valid @RequestParam String employeeNo) {
        return Result.success(feishuService.getFeishuUser(employeeNo));
    }

    @ApiOperation(value = "搜索所有飞书人员", notes = "搜索所有飞书人员")
    @GetMapping("/getAllFeishuUsers")
    public Result getAllFeishuUsers() {
        return Result.success(feishuService.listAllUsers());
    }

    @ApiOperation(value = "根据用户获取菜单栏", notes = "根据用户获取菜单栏")
    @GetMapping("/getMenuByEmployeeNo")
    public Result getMenuByEmployeeNo(@RequestParam String employNo) {
        return Result.success(feishuService.menuByEmployeeNo(employNo));
    }
}
