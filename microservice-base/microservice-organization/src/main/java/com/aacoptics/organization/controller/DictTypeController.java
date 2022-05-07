package com.aacoptics.organization.controller;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.organization.entity.form.DictTypeForm;
import com.aacoptics.organization.entity.form.DictTypeQueryForm;
import com.aacoptics.organization.entity.param.DictTypeQueryParam;
import com.aacoptics.organization.entity.po.DictType;
import com.aacoptics.organization.service.IDictTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 数据字典类型表
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/dictType")
@Api("DictTypeController")
@Slf4j
public class DictTypeController {
    @Resource
    private IDictTypeService dictTypeService;

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "搜索数据字典类型表", notes = "根据条件搜索数据字典类型表")
    @ApiImplicitParam(name = "dictTypeQueryForm", value = "数据字典类型表查询参数", required = true, dataType = "DictTypeQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping("/list")
    public Result<IPage<DictType>> list(@RequestBody DictTypeQueryForm dictTypeQueryForm) {
        log.debug("list with name:{}", dictTypeQueryForm);
        return Result.success(dictTypeService.listByAll(dictTypeQueryForm.getPage(), dictTypeQueryForm.toParam(DictTypeQueryParam.class)));
    }

    @ApiOperation(value = "获取数据字典类型表", notes = "获取数据字典类型表")
    @ApiImplicitParam(paramType = "path", name = "id", value = "数据字典类型表id", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result<Object> get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(dictTypeService.getByPrimaryKey(id));
    }


    @ApiOperation(value = "新增数据字典类型表", notes = "新增数据字典类型表")
    @ApiImplicitParam(name = "dictTypeForm", value = "新增数据字典类型表form表单", required = true, dataType = "DictTypeForm")
    @PostMapping
    public Result<Object> add(@Valid @RequestBody DictTypeForm dictTypeForm) {
        log.debug("add with name:{}", dictTypeForm);
        DictType dictType = dictTypeForm.toPo(DictType.class);
        return Result.success(dictTypeService.add(dictType));
    }

    @ApiOperation(value = "修改数据字典类型表", notes = "修改数据字典类型表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "数据字典类型表id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "dictTypeForm", value = "修改数据字典类型表form表单", required = true, dataType = "DictTypeForm")
    })
    @PutMapping(value = "/{id}")
    public Result<Object> update(@PathVariable Long id, @Valid @RequestBody DictTypeForm dictTypeForm) {
        log.debug("update with name:{}", dictTypeForm);
        DictType dictType = dictTypeForm.toPo(id, DictType.class);
        return Result.success(dictTypeService.update(dictType));
    }

    @ApiOperation(value = "删除数据字典类型表", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "数据字典类型表ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        log.debug("delete with id:{}", id);
        return Result.success(dictTypeService.delete(id));
    }

}
