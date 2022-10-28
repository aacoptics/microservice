package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.ProductLinePermissionForm;
import com.aacoptics.budget.report.entity.form.ProductLinePermissionQueryForm;
import com.aacoptics.budget.report.entity.param.ProductLinePermissionQueryParam;
import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.budget.report.service.ProductLinePermissionService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/productLinePermission")
@Api("productLinePermission")
@Slf4j
public class ProductLinePermissionController {

    @Autowired
    ProductLinePermissionService productLinePermissionService;


    @ApiOperation(value = "获取产品线权限", notes = "获取产品线权限")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(productLinePermissionService.getAll());
    }


    @ApiOperation(value = "搜索产品线权限", notes = "根据条件搜索产品线权限信息")
    @ApiImplicitParam(name = "productLinePermissionQueryForm", value = "产品线权限查询参数", required = true, dataType = "ProductLinePermissionQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProductLinePermissionQueryForm productLinePermissionQueryForm) {
        log.debug("query with name:{}", productLinePermissionQueryForm);
        return Result.success(productLinePermissionService.query(productLinePermissionQueryForm.getPage(),
                productLinePermissionQueryForm.toParam(ProductLinePermissionQueryParam.class)));
    }


    @ApiOperation(value = "新增产品线权限", notes = "新增一个产品线权限信息")
    @ApiImplicitParam(name = "productLinePermissionForm", value = "新增产品线权限form表单", required = true, dataType = "ProductLinePermissionForm")
    @PostMapping
    public Result add(@Valid @RequestBody ProductLinePermissionForm productLinePermissionForm) {
        log.debug("name:{}", productLinePermissionForm);
        ProductLinePermission productLinePermission = productLinePermissionForm.toPo(ProductLinePermission.class);
        return Result.success(productLinePermissionService.add(productLinePermission));
    }

    @ApiOperation(value = "删除产品线权限", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "产品线权限ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(productLinePermissionService.delete(id));
    }

    @ApiOperation(value = "修改产品线权限", notes = "修改指定产品线权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "产品线权限ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "productLinePermissionForm", value = "产品线权限实体", required = true, dataType = "ProductLinePermissionForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody ProductLinePermissionForm productLinePermissionForm) {
        ProductLinePermission productLinePermission = productLinePermissionForm.toPo(id, ProductLinePermission.class);
        return Result.success(productLinePermissionService.update(productLinePermission));
    }

    @ApiOperation(value = "获取产品线权限", notes = "获取指定产品线权限信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "产品线权限ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(productLinePermissionService.get(id));
    }
}