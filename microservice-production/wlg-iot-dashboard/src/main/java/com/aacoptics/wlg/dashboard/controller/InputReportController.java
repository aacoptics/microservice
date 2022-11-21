package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.po.InputReport;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/InputReport")
@Api("InputReport")
@Slf4j
public class InputReportController {

    @Resource()
    InputReportService inputReportService;


    @ApiOperation(value = "获取投入产出数据", notes = "获取投入产出数据")
    @PostMapping("/getByDateAndMachineName")
    public Result getByDateAndMachineName(@RequestBody InputReport inputReport) {
        return Result.success(inputReportService.getByDateAndMachineName(inputReport));
    }

    @ApiOperation(value = "更新投入产出数据", notes = "更新投入产出数据")
    @PostMapping("/updateOutPutInfo")
    public Result updateOutPutInfo(@RequestBody InputReport inputReport) {
        try{
            inputReportService.updateOutPutInfo(inputReport);
            return Result.success();
        }
        catch(Exception err){
            return Result.fail(err);
        }
    }
}