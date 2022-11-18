package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.aacoptics.wlg.dashboard.entity.po.MoldingParamThreshold;
import com.aacoptics.wlg.dashboard.service.MoldingMachineService;
import com.aacoptics.wlg.dashboard.service.MoldingParamThresholdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/moldingMachine")
@Api("moldingMachine")
@Slf4j
public class MoldingMachineController {

    @Resource
    MoldingMachineService moldingMachineService;

    @Resource
    MoldingParamThresholdService moldingParamThresholdService;

    @ApiOperation(value = "获取机台信息", notes = "获取机台信息")
    @GetMapping(value = "/getMachineInfo")
    public Result getMachineInfo(@RequestParam(required = false) String machineName,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        return Result.success(moldingMachineService.query(new Page(current, size), machineName));
    }

    @ApiOperation(value = "修改上料机状态", notes = "修改上料机状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "阈值ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "moldingParamThreshold", value = "阈值实体", required = true, dataType = "MoldingParamThreshold")})
    @PutMapping(value = "/{id}")
    public Result updateFeedingStatus(@PathVariable Integer id, @RequestBody MoldingMachine moldingMachine) {
        moldingMachine.setId(id);
        return Result.success(moldingMachineService.update(moldingMachine));
    }

    @ApiOperation(value = "新增阈值", notes = "新增阈值")
    @ApiImplicitParam(name = "moldingParamThreshold", value = "新增阈值表单", required = true, dataType = "MoldingParamThreshold")
    @PostMapping("/MoldingParamThreshold")
    public Result add(@Valid @RequestBody MoldingParamThreshold moldingParamThreshold) {
        return Result.success(moldingParamThresholdService.add(moldingParamThreshold));
    }

    @ApiOperation(value = "删除阈值", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "阈值ID", required = true, dataType = "Integer")
    @DeleteMapping(value = "/MoldingParamThreshold/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(moldingParamThresholdService.delete(id));
    }

    @ApiOperation(value = "修改阈值", notes = "修改指定阈值")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "阈值ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "moldingParamThreshold", value = "阈值实体", required = true, dataType = "MoldingParamThreshold")})
    @PutMapping(value = "/MoldingParamThreshold/{id}")
    public Result update(@PathVariable Integer id, @RequestBody MoldingParamThreshold moldingParamThreshold) {
        moldingParamThreshold.setId(id);
        return Result.success(moldingParamThresholdService.update(moldingParamThreshold));
    }

    @ApiOperation(value = "获取阈值信息", notes = "获取阈值信息")
    @GetMapping(value = "/getParamThreshold")
    public Result getParamThreshold(@RequestParam Integer machineId,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        return Result.success(moldingParamThresholdService.query(new Page(current, size), machineId));
    }
}