package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.EquipmentForm;
import com.aacoptics.wlg.equipment.entity.form.EquipmentQueryForm;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.service.EquipmentService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/equipmentManagement")
@Api("equipmentManagement")
@Slf4j
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;



    @ApiOperation(value = "搜索设备", notes = "根据条件搜索设备信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.query(equipmentQueryForm.getPage(), equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }



    @ApiOperation(value = "搜索设备名称", notes = "根据条件搜索设备名称")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findMchNameList")
    public Result findMchNameList() {
        return Result.success(equipmentService.findMchNameList());
    }


    @ApiOperation(value = "搜索设备规格", notes = "根据条件搜索设备规格")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备规格查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findSpecListByMchName")
    public Result findSpecListByMchName(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.findSpecListByMchName(equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }


    @ApiOperation(value = "搜索设备型号", notes = "根据条件搜索设备型号")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "设备型号查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findTypeVersionListByMchNameAndSpec")
    public Result findTypeVersionListByMchNameAndSpec(@Valid @RequestBody EquipmentQueryForm equipmentQueryForm) {
        log.debug("query with name:{}", equipmentQueryForm);
        return Result.success(equipmentService.findTypeVersionListByMchNameAndSpec(equipmentQueryForm.toParam(EquipmentQueryParam.class)));
    }


    @ApiOperation(value = "新增设备", notes = "新增一个设备信息")
    @ApiImplicitParam(name = "equipmentForm", value = "新增设备form表单", required = true, dataType = "EquipmentForm")
    @PostMapping
    public Result add(@Valid @RequestBody EquipmentForm equipmentForm) {
        log.debug("name:{}", equipmentForm);
        Equipment equipment = equipmentForm.toPo(Equipment.class);
        return Result.success(equipmentService.add(equipment));
    }

    @ApiOperation(value = "删除设备", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "设备ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(equipmentService.delete(id));
    }

    @ApiOperation(value = "修改设备", notes = "修改指定设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "equipmentForm", value = "设备实体", required = true, dataType = "EquipmentForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody EquipmentForm equipmentForm) {
        Equipment equipment = equipmentForm.toPo(id, Equipment.class);
        return Result.success(equipmentService.update(equipment));
    }

    @ApiOperation(value = "获取设备", notes = "获取指定设备信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "设备ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(equipmentService.get(id));
    }
}