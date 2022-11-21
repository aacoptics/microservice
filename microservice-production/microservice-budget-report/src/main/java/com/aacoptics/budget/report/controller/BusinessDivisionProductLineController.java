package com.aacoptics.budget.report.controller;

import com.aacoptics.budget.report.entity.form.BusinessDivisionProductLineForm;
import com.aacoptics.budget.report.entity.form.BusinessDivisionProductLineQueryForm;
import com.aacoptics.budget.report.entity.param.BusinessDivisionProductLineQueryParam;
import com.aacoptics.budget.report.entity.po.BusinessDivisionProductLine;
import com.aacoptics.budget.report.service.BusinessDivisionProductLineService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/businessDivisionProductLine")
@Api("businessDivisionProductLine")
@Slf4j
public class BusinessDivisionProductLineController {

    @Autowired
    BusinessDivisionProductLineService businessDivisionProductLineService;


    @ApiOperation(value = "获取事业部产品线", notes = "获取事业部产品线")
    @GetMapping(value = "/all")
    public Result get() {
        return Result.success(businessDivisionProductLineService.getAll());
    }


    @ApiOperation(value = "获取事业部", notes = "获取事业部")
    @GetMapping(value = "/getAllBusinessDivision")
    public Result getAllBusinessDivision() {
        return Result.success(businessDivisionProductLineService.getAllBusinessDivision());
    }

    @ApiOperation(value = "获取产品线", notes = "获取产品线")
    @PostMapping(value = "/getProductLineByBusinessDivision")
    public Result getProductLineByBusinessDivision(@Valid @RequestBody BusinessDivisionProductLineQueryForm businessDivisionProductLineQueryForm) {
        return Result.success(businessDivisionProductLineService.getProductLineByBusinessDivision(businessDivisionProductLineQueryForm.getBusinessDivision()));
    }

    @ApiOperation(value = "搜索事业部产品线", notes = "根据条件搜索事业部产品线信息")
    @ApiImplicitParam(name = "businessDivisionProductLineQueryForm", value = "事业部产品线查询参数", required = true, dataType = "BusinessDivisionProductLineQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody BusinessDivisionProductLineQueryForm businessDivisionProductLineQueryForm) {
        log.debug("query with name:{}", businessDivisionProductLineQueryForm);
        return Result.success(businessDivisionProductLineService.query(businessDivisionProductLineQueryForm.getPage(),
                businessDivisionProductLineQueryForm.toParam(BusinessDivisionProductLineQueryParam.class)));
    }


    @ApiOperation(value = "新增事业部产品线", notes = "新增一个事业部产品线信息")
    @ApiImplicitParam(name = "businessDivisionProductLineForm", value = "新增事业部产品线form表单", required = true, dataType = "BusinessDivisionProductLineForm")
    @PostMapping
    public Result add(@Valid @RequestBody BusinessDivisionProductLineForm businessDivisionProductLineForm) {
        log.debug("name:{}", businessDivisionProductLineForm);
        BusinessDivisionProductLine businessDivisionProductLine = businessDivisionProductLineForm.toPo(BusinessDivisionProductLine.class);
        return Result.success(businessDivisionProductLineService.add(businessDivisionProductLine));
    }

    @ApiOperation(value = "删除事业部产品线", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "事业部产品线ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(businessDivisionProductLineService.delete(id));
    }

    @ApiOperation(value = "修改事业部产品线", notes = "修改指定事业部产品线信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "事业部产品线ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "businessDivisionProductLineForm", value = "事业部产品线实体", required = true, dataType = "BusinessDivisionProductLineForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody BusinessDivisionProductLineForm businessDivisionProductLineForm) {
        BusinessDivisionProductLine businessDivisionProductLine = businessDivisionProductLineForm.toPo(id, BusinessDivisionProductLine.class);
        return Result.success(businessDivisionProductLineService.update(businessDivisionProductLine));
    }

    @ApiOperation(value = "获取事业部产品线", notes = "获取指定事业部产品线信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "事业部产品线ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(businessDivisionProductLineService.get(id));
    }
}