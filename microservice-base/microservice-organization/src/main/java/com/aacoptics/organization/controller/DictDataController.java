package com.aacoptics.organization.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.organization.entity.form.DictDataForm;
import com.aacoptics.organization.entity.form.DictDataQueryForm;
import com.aacoptics.organization.entity.param.DictDataQueryParam;
import com.aacoptics.organization.entity.po.DictData;
import com.aacoptics.organization.entity.po.DictType;
import com.aacoptics.organization.service.IDictDataService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 字典数据表
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/dictData")
@Api("DictDataController")
@Slf4j
public class DictDataController {

    @Resource
    private IDictDataService dictDataService;

    @ApiOperation(value = "根据字典类型查询字典数据信息", notes = "根据字典类型查询字典数据信息")
    @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, dataType = "String")
    @GetMapping(value = "/type/{dictType}")
    public Result<List<DictData>> dictType(@PathVariable String dictType) {
        log.debug("dictType with path:{}", dictType);

        return Result.success(dictDataService.listByType(dictType));
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "搜索数据字典数据表", notes = "根据条件搜索数据字典数据表")
    @ApiImplicitParam(name = "dictDataQueryForm", value = "数据字典数据表查询参数", required = true, dataType = "DictDataQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping("/list")
    public Result<IPage<DictType>> list(@RequestBody DictDataQueryForm dictDataQueryForm) {
        log.debug("list with name:{}", dictDataQueryForm);
        return Result.success(dictDataService.listByAll(dictDataQueryForm.getPage(), dictDataQueryForm.toParam(DictDataQueryParam.class)));
    }

    @ApiOperation(value = "新增字典数据表", notes = "新增字典数据表")
    @ApiImplicitParam(name = "dictDataForm", value = "新增字典数据表form表单", required = true, dataType = "DictDataForm")
    @PostMapping
    public Result<Object> add(@Valid @RequestBody DictDataForm dictDataForm) {
        log.debug("add with name:{}", dictDataForm);
        DictData dictData = dictDataForm.toPo(DictData.class);
        return Result.success(dictDataService.add(dictData));
    }

    @ApiOperation(value = "修改字典数据表", notes = "修改字典数据表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典数据表id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "dictDataForm", value = "修改字典数据表form表单", required = true, dataType = "DictDataForm")
    })
    @PutMapping(value = "/{id}")
    public Result<Object> update(@PathVariable Long id, @Valid @RequestBody DictDataForm dictDataForm) {
        log.debug("update with name:{}", dictDataForm);
        DictData dictData = dictDataForm.toPo(id, DictData.class);
        return Result.success(dictDataService.update(dictData));
    }

    @ApiOperation(value = "删除字典数据表", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "字典数据表ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        log.debug("delete with id:{}", id);
        return Result.success(dictDataService.delete(id));
    }

}
