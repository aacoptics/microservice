package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.entity.form.FanucCheckItemThresholdForm;
import com.aacoptics.fanuc.dashboard.entity.form.FanucCheckItemThresholdQueryForm;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckItemThresholdParam;
import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import com.aacoptics.fanuc.dashboard.exception.BusinessException;
import com.aacoptics.fanuc.dashboard.service.FanucCheckItemThresholdService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/fanucCheckItemThreshold")
@Api("fanucCheckItemThreshold")
@Slf4j
public class FanucCheckItemThresholdController {

    @Autowired
    FanucCheckItemThresholdService fanucCheckItemThresholdService;



    @ApiOperation(value = "搜索检查项阈值", notes = "根据条件搜索检查项阈值信息")
    @ApiImplicitParam(name = "fanucCheckItemThresholdQueryForm", value = "检查项阈值查询参数", required = true, dataType = "FanucCheckItemThresholdQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody FanucCheckItemThresholdQueryForm fanucCheckItemThresholdQueryForm) {
        log.debug("query with name:{}", fanucCheckItemThresholdQueryForm);
        return Result.success(fanucCheckItemThresholdService.query(fanucCheckItemThresholdQueryForm.getPage(), fanucCheckItemThresholdQueryForm.toParam(FanucCheckItemThresholdParam.class)));
    }

    @ApiOperation(value = "新增检查项阈值", notes = "新增一个检查项阈值信息")
    @ApiImplicitParam(name = "fanucCheckItemThresholdForm", value = "新增检查项阈值form表单", required = true, dataType = "FanucCheckItemThresholdForm")
    @PostMapping
    public Result add(@Valid @RequestBody FanucCheckItemThresholdForm fanucCheckItemThresholdForm) {
        log.debug("name:{}", fanucCheckItemThresholdForm);
        FanucCheckItemThreshold fanucCheckItemThreshold = fanucCheckItemThresholdForm.toPo(FanucCheckItemThreshold.class);
        return Result.success(fanucCheckItemThresholdService.add(fanucCheckItemThreshold));
    }

    @ApiOperation(value = "删除检查项阈值", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "检查项阈值ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(fanucCheckItemThresholdService.delete(id));
    }

    @ApiOperation(value = "修改检查项阈值", notes = "修改指定检查项阈值信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "检查项阈值ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "fanucCheckItemThresholdForm", value = "检查项阈值实体", required = true, dataType = "FanucCheckItemThresholdForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody FanucCheckItemThresholdForm fanucCheckItemThresholdForm) {
        FanucCheckItemThreshold fanucCheckItemThreshold = fanucCheckItemThresholdForm.toPo(id, FanucCheckItemThreshold.class);
        return Result.success(fanucCheckItemThresholdService.update(fanucCheckItemThreshold));
    }

    @ApiOperation(value = "获取检查项阈值", notes = "获取指定检查项阈值信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "检查项阈值ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(fanucCheckItemThresholdService.get(id));
    }


}