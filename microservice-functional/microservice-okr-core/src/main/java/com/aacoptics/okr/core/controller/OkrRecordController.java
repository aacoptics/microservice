package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.entity.po.ProcessRecord;
import com.aacoptics.okr.core.service.ActionDetailService;
import com.aacoptics.okr.core.service.ProcessRecordService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/aacOkrRecord")
@Api("aacOkrRecord")
@Slf4j
public class OkrRecordController {
    @Resource
    private ProcessRecordService processRecordService;

    @ApiOperation(value = "新增Record", notes = "新增Record")
    @ApiImplicitParam(name = "actionDetail", value = "新增Action表单", required = true, dataType = "ActionDetail")
    @PostMapping
    public Result add(@Valid @RequestBody ProcessRecord processRecord) {
        return Result.success(processRecordService.add(processRecord));
    }

    @ApiOperation(value = "删除Record", notes = "根据url的id来指定删除Record")
    @ApiImplicitParam(paramType = "path", name = "id", value = "Record ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteRecord/{id}")
    public Result deleteRecord(@PathVariable Long id) {
        return Result.success(processRecordService.delete(id));
    }

    @ApiOperation(value = "更新或插入Record", notes = "更新或插入Record")
    @ApiImplicitParam(name = "processRecord", value = "新增OKR表单", required = true, dataType = "ProcessRecord")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/addOrUpdateAction")
    public Result addOrUpdateAction(@RequestBody ProcessRecord processRecord) {
        boolean res = processRecordService.addOrUpdateAction(processRecord);
        return res? Result.success() : Result.fail("更新失败！");
    }


    @ApiOperation(value = "获取Record", notes = "获取Record")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/getById")
    public Result getById(@RequestParam Long id, @RequestParam Integer type) {
        return Result.success(processRecordService.listAllById(id, type));
    }
}