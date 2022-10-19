package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.BudgetUploadLogQueryForm;
import com.aacoptics.budget.report.entity.param.BudgetUploadLogQueryParam;
import com.aacoptics.budget.report.service.BudgetUploadLogService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/budgetUploadLog")
@Api("budgetUploadLog")
@Slf4j
public class BudgetUploadLogController {

    @Autowired
    BudgetUploadLogService budgetUploadLogService;


    @ApiOperation(value = "搜索上传日志", notes = "根据条件搜索上传日志信息")
    @ApiImplicitParam(name = "budgetUploadLogQueryForm", value = "上传日志查询参数", required = true, dataType = "BudgetUploadLogQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody BudgetUploadLogQueryForm budgetUploadLogQueryForm) {
        log.debug("query with name:{}", budgetUploadLogQueryForm);
        return Result.success(budgetUploadLogService.query(budgetUploadLogQueryForm.getPage(), budgetUploadLogQueryForm.toParam(BudgetUploadLogQueryParam.class)));
    }


    @ApiOperation(value = "删除上传日志", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传日志ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(budgetUploadLogService.delete(id));
    }

    @ApiOperation(value = "获取上传日志", notes = "获取指定上传日志信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "上传日志ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(budgetUploadLogService.get(id));
    }

}