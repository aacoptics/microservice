package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.RepairOrderForm;
import com.aacoptics.wlg.equipment.entity.form.RepairOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.service.RepairOrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/repairOrder")
@Api("repairOrder")
@Slf4j
public class RepairOrderController {

    @Autowired
    RepairOrderService repairOrderService;


    @ApiOperation(value = "搜索维修工单", notes = "根据条件搜索维修工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "维修工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody RepairOrderQueryForm repairOrderQueryForm) {
        log.debug("query with name:{}", repairOrderQueryForm);
        return Result.success(repairOrderService.query(repairOrderQueryForm.getPage(), repairOrderQueryForm.toParam(RepairOrderQueryParam.class)));
    }

    @ApiOperation(value = "新增维修工单", notes = "新增一个维修工单信息")
    @ApiImplicitParam(name = "RepairForm", value = "新增维修工单form表单", required = true, dataType = "RepairOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody RepairOrderForm repairOrderForm) {
        log.debug("name:{}", repairOrderForm);
        RepairOrder repairOrder = repairOrderForm.toPo(RepairOrder.class);
        return Result.success(repairOrderService.add(repairOrder));
    }

    @ApiOperation(value = "删除维修工单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "维修工单ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(repairOrderService.delete(id));
    }

    @ApiOperation(value = "修改维修工单", notes = "修改指定维修工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "维修工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "RepairForm", value = "维修工单实体", required = true, dataType = "RepairOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RepairOrderForm repairOrderForm) {
        RepairOrder repairOrder = repairOrderForm.toPo(id, RepairOrder.class);
        return Result.success(repairOrderService.update(repairOrder));
    }

    @ApiOperation(value = "获取维修工单", notes = "获取指定维修工单信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "维修工单ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(repairOrderService.get(id));
    }

}