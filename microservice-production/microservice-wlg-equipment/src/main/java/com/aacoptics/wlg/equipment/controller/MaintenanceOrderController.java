package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/maintenanceOrder")
@Api("maintenanceOrder")
@Slf4j
public class MaintenanceOrderController {

    @Autowired
    MaintenanceOrderService maintenanceOrderService;

    @Autowired
    MaintenanceItemService maintenanceItemService;


    @ApiOperation(value = "搜索保养工单", notes = "根据条件搜索保养工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "保养工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm) {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        return Result.success(maintenanceOrderService.query(maintenanceOrderQueryForm.getPage(), maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class)));
    }

    @ApiOperation(value = "新增保养工单", notes = "新增一个保养工单信息")
    @ApiImplicitParam(name = "MaintenanceForm", value = "新增保养工单form表单", required = true, dataType = "MaintenanceOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        log.debug("name:{}", maintenanceOrderForm);
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.add(maintenanceOrder));
    }

    @ApiOperation(value = "删除保养工单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养工单ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(maintenanceOrderService.delete(id));
    }

    @ApiOperation(value = "修改保养工单", notes = "修改指定保养工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "保养工单实体", required = true, dataType = "MaintenanceOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(id, MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.update(maintenanceOrder));
    }

    @ApiOperation(value = "获取保养工单", notes = "获取指定保养工单信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养工单ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(maintenanceOrderService.get(id));
    }

}