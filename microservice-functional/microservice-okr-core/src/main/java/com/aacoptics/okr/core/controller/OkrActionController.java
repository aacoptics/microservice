package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.service.ActionDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/aacOkrAction")
@Api("aacOkrAction")
@Slf4j
public class OkrActionController {
    @Resource
    private ActionDetailService actionDetailService;

    @ApiOperation(value = "新增Action", notes = "新增Action")
    @ApiImplicitParam(name = "actionDetail", value = "新增Action表单", required = true, dataType = "ActionDetail")
    @PostMapping
    public Result add(@Valid @RequestBody ActionDetail actionDetail) {
        return Result.success(actionDetailService.add(actionDetail));
    }

    @ApiOperation(value = "删除Action", notes = "根据url的id来指定删除Action")
    @ApiImplicitParam(paramType = "path", name = "id", value = "Action ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteAction/{id}")
    public Result deleteAction(@PathVariable Long id) {
        return Result.success(actionDetailService.deleteAction(id));
    }

    @ApiOperation(value = "更新或插入Action", notes = "更新或插入Action")
    @ApiImplicitParam(name = "actionDetail", value = "新增OKR表单", required = true, dataType = "ActionDetail")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/addOrUpdateAction")
    public Result addOrUpdateAction(@RequestBody ActionDetail actionDetail) {
        Boolean res = actionDetailService.addOrUpdateAction(actionDetail);
        return res ? Result.success() : Result.fail("更新失败！");
    }


    @ApiOperation(value = "获取action", notes = "获取action")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/getById")
    public Result getById(@RequestParam Long id) {
        return Result.success(actionDetailService.getById(id));
    }
}
