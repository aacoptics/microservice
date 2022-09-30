package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.InspectionItemForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionMainForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionQueryForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionShiftForm;
import com.aacoptics.wlg.equipment.entity.param.InspectionQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionItem;
import com.aacoptics.wlg.equipment.entity.po.InspectionMain;
import com.aacoptics.wlg.equipment.entity.po.InspectionShift;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionMainService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/inspectionManagement")
@Api("inspectionManagement")
@Slf4j
public class InspectionController {

    @Autowired
    InspectionMainService inspectionMainService;

    @Autowired
    InspectionItemService inspectionItemService;

    @Autowired
    InspectionShiftService inspectionShiftService;

    @ApiOperation(value = "搜索点检", notes = "根据条件搜索点检信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "点检查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody InspectionQueryForm inspectionQueryForm) {
        log.debug("query with name:{}", inspectionQueryForm);
        return Result.success(inspectionMainService.query(inspectionQueryForm.getPage(), inspectionQueryForm.toParam(InspectionQueryParam.class)));
    }

    @ApiOperation(value = "新增点检", notes = "新增一个点检信息")
    @ApiImplicitParam(name = "InspectionForm", value = "新增点检form表单", required = true, dataType = "InspectionMainForm")
    @PostMapping
    public Result add(@Valid @RequestBody InspectionMainForm inspectionMainForm) {
        log.debug("name:{}", inspectionMainForm);
        InspectionMain inspectionMain = inspectionMainForm.toPo(InspectionMain.class);
        return Result.success(inspectionMainService.add(inspectionMain));
    }

    @ApiOperation(value = "删除点检", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(inspectionMainService.delete(id));
    }

    @ApiOperation(value = "修改点检", notes = "修改指定点检信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "点检实体", required = true, dataType = "InspectionMainForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody InspectionMainForm inspectionMainForm) {
        InspectionMain inspectionMain = inspectionMainForm.toPo(id, InspectionMain.class);
        return Result.success(inspectionMainService.update(inspectionMain));
    }

    @ApiOperation(value = "获取点检", notes = "获取指定点检信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(inspectionMainService.get(id));
    }


    @ApiOperation(value = "新增点检项", notes = "新增一个点检项信息")
    @ApiImplicitParam(name = "InspectionItemForm", value = "新增点检form表单", required = true, dataType = "InspectionItemForm")
    @PostMapping(value = "/addInspectionItem")
    public Result addInspectionItem(@Valid @RequestBody InspectionItemForm inspectionItemForm) {
        log.debug("name:{}", inspectionItemForm);
        InspectionItem inspectionItem = inspectionItemForm.toPo(InspectionItem.class);
        return Result.success(inspectionItemService.add(inspectionItem));
    }

    @ApiOperation(value = "删除点检项", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteInspectionItem/{id}")
    public Result deleteInspectionItem(@PathVariable Long id) {
        return Result.success(inspectionItemService.delete(id));
    }

    @ApiOperation(value = "修改点检", notes = "修改指定点检项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionItemForm", value = "点检项实体", required = true, dataType = "InspectionItemForm")
    })
    @PutMapping(value = "/updateInspectionItem/{id}")
    public Result updateInspectionItem(@PathVariable Long id, @Valid @RequestBody InspectionItemForm inspectionItemForm) {
        InspectionItem inspectionItem = inspectionItemForm.toPo(id, InspectionItem.class);
        return Result.success(inspectionItemService.update(inspectionItem));
    }


    @ApiOperation(value = "新增点检班次", notes = "新增一个点检班次信息")
    @ApiImplicitParam(name = "InspectionItemForm", value = "新增点检form表单", required = true, dataType = "InspectionItemForm")
    @PostMapping(value = "/addInspectionShift")
    public Result addInspectionShift(@Valid @RequestBody InspectionShiftForm inspectionShiftForm) {
        log.debug("name:{}", inspectionShiftForm);
        InspectionShift inspectionShift = inspectionShiftForm.toPo(InspectionShift.class);
        return Result.success(inspectionShiftService.add(inspectionShift));
    }

    @ApiOperation(value = "删除点检班次", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteInspectionShift/{id}")
    public Result deleteInspectionShift(@PathVariable Long id) {
        return Result.success(inspectionShiftService.delete(id));
    }

    @ApiOperation(value = "修改点检班次", notes = "修改指定点检班次信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionItemForm", value = "点检班次实体", required = true, dataType = "InspectionItemForm")
    })
    @PutMapping(value = "/updateInspectionShift/{id}")
    public Result updateInspectionShift(@PathVariable Long id, @Valid @RequestBody InspectionShiftForm inspectionShiftForm) {
        InspectionShift inspectionShift = inspectionShiftForm.toPo(id, InspectionShift.class);
        return Result.success(inspectionShiftService.update(inspectionShift));
    }
}