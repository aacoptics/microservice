package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.aacoptics.okr.core.service.PeriodInfoService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/aacOkrPeriod")
@Api("aacOkrPeriod")
@Slf4j
public class OkrPeriodController {
    @Resource
    private PeriodInfoService periodInfoService;
    @ApiOperation(value = "新增OKR周期", notes = "新增OKR周期")
    @ApiImplicitParam(name = "periodInfo", value = "新增周期表单", required = true, dataType = "PeriodInfo")
    @PostMapping
    public Result add(@Valid @RequestBody PeriodInfo periodInfo) {
        return Result.success(periodInfoService.add(periodInfo));
    }

    @ApiOperation(value = "删除OKR周期", notes = "根据url的id来指定删除OKR周期")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(periodInfoService.delete(id));
    }


    @ApiOperation(value = "修改OKR周期", notes = "修改指定OKR周期")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "OKR周期ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "periodInfo", value = "OKR周期实体", required = true, dataType = "PeriodInfo")})
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody PeriodInfo periodInfo) {
        periodInfo.setId(id);
        return Result.success(periodInfoService.update(periodInfo));
    }

    @ApiOperation(value = "获取所有OKR周期", notes = "获取所有OKR周期")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/listAll")
    public Result listAll() {
        return Result.success(periodInfoService.listAll());
    }
}