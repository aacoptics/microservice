package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.ProjectMapForm;
import com.aacoptics.wlg.report.entity.form.ProjectMapQueryForm;
import com.aacoptics.wlg.report.entity.param.ProjectMapQueryParam;
import com.aacoptics.wlg.report.entity.po.ProjectMap;
import com.aacoptics.wlg.report.service.ProjectMapService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/projectMap")
@Api("projectMap")
@Slf4j
public class ProjectMapController {

    @Autowired
    ProjectMapService projectMapService;


    @ApiOperation(value = "获取项目映射", notes = "获取项目映射")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(projectMapService.getAll());
    }


    @ApiOperation(value = "搜索项目映射", notes = "根据条件搜索项目映射信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "项目映射查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody ProjectMapQueryForm projectMapQueryForm) {
        log.debug("query with name:{}", projectMapQueryForm);
        return Result.success(projectMapService.query(projectMapQueryForm.getPage(), projectMapQueryForm.toParam(ProjectMapQueryParam.class)));
    }


    @ApiOperation(value = "新增项目映射", notes = "新增一个项目映射信息")
    @ApiImplicitParam(name = "projectMapForm", value = "新增项目映射form表单", required = true, dataType = "ProjectMapForm")
    @PostMapping
    public Result add(@Valid @RequestBody ProjectMapForm projectMapForm) {
        log.debug("name:{}", projectMapForm);
        ProjectMap projectMap = projectMapForm.toPo(ProjectMap.class);
        return Result.success(projectMapService.add(projectMap));
    }

    @ApiOperation(value = "删除项目映射", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "项目映射ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(projectMapService.delete(id));
    }

    @ApiOperation(value = "修改项目映射", notes = "修改指定项目映射信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目映射ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "projectMapForm", value = "项目映射实体", required = true, dataType = "ProjectMapForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody ProjectMapForm projectMapForm) {
        ProjectMap projectMap = projectMapForm.toPo(id, ProjectMap.class);
        return Result.success(projectMapService.update(projectMap));
    }

    @ApiOperation(value = "获取项目映射", notes = "获取指定项目映射信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "项目映射ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(projectMapService.get(id));
    }
}