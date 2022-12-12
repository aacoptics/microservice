package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.*;
import com.aacoptics.wlg.equipment.entity.param.ExceptionTypeQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/exceptionTypeManagement")
@Api("exceptionTypeManagement")
@Slf4j
public class ExceptionTypeController {

    @Autowired
    ExceptionTypeService exceptionTypeService;

    @Autowired
    ExceptionSubClassService exceptionSubClassService;


    @ApiOperation(value = "搜索异常类型", notes = "根据条件搜索异常类型信息")
    @ApiImplicitParam(name = "exceptionTypeQueryForm", value = "异常类型查询参数", required = true, dataType = "ExceptionTypeQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ExceptionTypeQueryForm exceptionTypeQueryForm) {
        log.debug("query with name:{}", exceptionTypeQueryForm);
        return Result.success(exceptionTypeService.query(exceptionTypeQueryForm.getPage(), exceptionTypeQueryForm.toParam(ExceptionTypeQueryParam.class)));
    }

    @ApiOperation(value = "新增异常类型", notes = "新增一个异常类型信息")
    @ApiImplicitParam(name = "exceptionTypeForm", value = "新增异常类型form表单", required = true, dataType = "ExceptionTypeForm")
    @PostMapping
    public Result add(@Valid @RequestBody ExceptionTypeForm exceptionTypeForm) {
        log.debug("name:{}", exceptionTypeForm);
        ExceptionType exceptionType = exceptionTypeForm.toPo(ExceptionType.class);
        return Result.success(exceptionTypeService.add(exceptionType));
    }

    @ApiOperation(value = "删除异常类型", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "异常类型ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(exceptionTypeService.delete(id));
    }

    @ApiOperation(value = "修改异常类型", notes = "修改指定异常类型信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "异常类型ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "异常类型实体", required = true, dataType = "InspectionMainForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody ExceptionTypeForm exceptionTypeForm) {
        ExceptionType exceptionType = exceptionTypeForm.toPo(id, ExceptionType.class);
        return Result.success(exceptionTypeService.update(exceptionType));
    }

    @ApiOperation(value = "获取异常类型", notes = "获取指定异常类型信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "异常类型ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(exceptionTypeService.get(id));
    }

    @ApiOperation(value = "获取异常类型", notes = "获取指定异常类型信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "异常类型ID", example = "0", required = true, dataType = "Long")
    @PostMapping(value = "/findById")
    public Result findById(@Valid @RequestBody ExceptionTypeQueryForm exceptionTypeQueryForm) {
        log.debug("get with exceptionTypeQueryForm:{}", exceptionTypeQueryForm);
        if(exceptionTypeQueryForm.getId() == null)
        {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(exceptionTypeService.get(exceptionTypeQueryForm.getId()));
    }

    @ApiOperation(value = "新增异常类型项", notes = "新增一个异常类型项信息")
    @ApiImplicitParam(name = "exceptionSubClassForm", value = "新增异常类型form表单", required = true, dataType = "ExceptionSubClassForm")
    @PostMapping(value = "/addExceptionSubClass")
    public Result addInspectionItem(@Valid @RequestBody ExceptionSubClassForm exceptionSubClassForm) {
        log.debug("name:{}", exceptionSubClassForm);
        ExceptionSubClass exceptionSubClass = exceptionSubClassForm.toPo(ExceptionSubClass.class);
        return Result.success(exceptionSubClassService.add(exceptionSubClass));
    }

    @ApiOperation(value = "删除异常类型项", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "异常类型ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteExceptionSubClass/{id}")
    public Result deleteInspectionItem(@PathVariable Long id) {
        return Result.success(exceptionSubClassService.delete(id));
    }

    @ApiOperation(value = "修改异常类型", notes = "修改指定异常类型项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "异常类型ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "exceptionSubClassForm", value = "异常类型项实体", required = true, dataType = "ExceptionSubClassForm")
    })
    @PutMapping(value = "/updateExceptionSubClass/{id}")
    public Result updateInspectionItem(@PathVariable Long id, @Valid @RequestBody ExceptionSubClassForm exceptionSubClassForm) {
        ExceptionSubClass exceptionSubClass = exceptionSubClassForm.toPo(id, ExceptionSubClass.class);
        return Result.success(exceptionSubClassService.update(exceptionSubClass));
    }


    @ApiOperation(value = "获取异常分类", notes = "获取异常分类")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findExceptionTypeList")
    public Result findExceptionTypeList() {
        return Result.success(exceptionTypeService.findExceptionTypeList());
    }

}