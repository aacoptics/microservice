package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceItemForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceMainForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceMain;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceMainService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/maintenanceManagement")
@Api("maintenanceManagement")
@Slf4j
public class MaintenanceController {

    @Autowired
    MaintenanceMainService maintenanceMainService;

    @Autowired
    MaintenanceItemService maintenanceItemService;


    @ApiOperation(value = "搜索保养", notes = "根据条件搜索保养信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "保养查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MaintenanceQueryForm MaintenanceQueryForm) {
        log.debug("query with name:{}", MaintenanceQueryForm);
        return Result.success(maintenanceMainService.query(MaintenanceQueryForm.getPage(), MaintenanceQueryForm.toParam(MaintenanceQueryParam.class)));
    }

    @ApiOperation(value = "新增保养", notes = "新增一个保养信息")
    @ApiImplicitParam(name = "MaintenanceForm", value = "新增保养form表单", required = true, dataType = "MaintenanceMainForm")
    @PostMapping
    public Result add(@Valid @RequestBody MaintenanceMainForm maintenanceMainForm) {
        log.debug("name:{}", maintenanceMainForm);
        MaintenanceMain maintenanceMain = maintenanceMainForm.toPo(MaintenanceMain.class);
        return Result.success(maintenanceMainService.add(maintenanceMain));
    }

    @ApiOperation(value = "删除保养", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(maintenanceMainService.delete(id));
    }

    @ApiOperation(value = "修改保养", notes = "修改指定保养信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "保养实体", required = true, dataType = "MaintenanceMainForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MaintenanceMainForm maintenanceMainForm) {
        MaintenanceMain maintenanceMain = maintenanceMainForm.toPo(id, MaintenanceMain.class);
        return Result.success(maintenanceMainService.update(maintenanceMain));
    }

    @ApiOperation(value = "获取保养", notes = "获取指定保养信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(maintenanceMainService.get(id));
    }


    @ApiOperation(value = "新增保养项", notes = "新增一个保养项信息")
    @ApiImplicitParam(name = "MaintenanceItemForm", value = "新增保养form表单", required = true, dataType = "MaintenanceItemForm")
    @PostMapping(value = "/addMaintenanceItem")
    public Result addMaintenanceItem(@Valid @RequestBody MaintenanceItemForm maintenanceItemForm) {
        log.debug("name:{}", maintenanceItemForm);
        MaintenanceItem maintenanceItem = maintenanceItemForm.toPo(MaintenanceItem.class);
        return Result.success(maintenanceItemService.add(maintenanceItem));
    }

    @ApiOperation(value = "删除保养项", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteMaintenanceItem/{id}")
    public Result deleteMaintenanceItem(@PathVariable Long id) {
        return Result.success(maintenanceItemService.delete(id));
    }

    @ApiOperation(value = "修改保养", notes = "修改指定保养项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceItemForm", value = "保养项实体", required = true, dataType = "MaintenanceItemForm")
    })
    @PutMapping(value = "/updateMaintenanceItem/{id}")
    public Result updateMaintenanceItem(@PathVariable Long id, @Valid @RequestBody MaintenanceItemForm maintenanceItemForm) {
        MaintenanceItem maintenanceItem = maintenanceItemForm.toPo(id, MaintenanceItem.class);
        return Result.success(maintenanceItemService.update(maintenanceItem));
    }

}