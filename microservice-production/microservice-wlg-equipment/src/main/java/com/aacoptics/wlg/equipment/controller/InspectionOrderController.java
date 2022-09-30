package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.InspectionOrderForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inspectionOrder")
@Api("inspectionOrder")
@Slf4j
public class InspectionOrderController {

    @Autowired
    InspectionOrderService inspectionOrderService;

    @Autowired
    InspectionItemService inspectionItemService;

    @Autowired
    InspectionShiftService inspectionShiftService;

    @ApiOperation(value = "搜索点检工单", notes = "根据条件搜索点检工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "点检工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody InspectionOrderQueryForm inspectionOrderQueryForm) {
        log.debug("query with name:{}", inspectionOrderQueryForm);
        return Result.success(inspectionOrderService.query(inspectionOrderQueryForm.getPage(), inspectionOrderQueryForm.toParam(InspectionOrderQueryParam.class)));
    }

    @ApiOperation(value = "新增点检工单", notes = "新增一个点检工单信息")
    @ApiImplicitParam(name = "InspectionForm", value = "新增点检工单form表单", required = true, dataType = "InspectionOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody InspectionOrderForm inspectionOrderForm) {
        log.debug("name:{}", inspectionOrderForm);
        InspectionOrder inspectionOrder = inspectionOrderForm.toPo(InspectionOrder.class);
        return Result.success(inspectionOrderService.add(inspectionOrder));
    }

    @ApiOperation(value = "删除点检工单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检工单ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(inspectionOrderService.delete(id));
    }

    @ApiOperation(value = "修改点检工单", notes = "修改指定点检工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "点检工单实体", required = true, dataType = "InspectionOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody InspectionOrderForm inspectionOrderForm) {
        InspectionOrder inspectionOrder = inspectionOrderForm.toPo(id, InspectionOrder.class);
        return Result.success(inspectionOrderService.update(inspectionOrder));
    }

    @ApiOperation(value = "获取点检工单", notes = "获取指定点检工单信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "点检工单ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(inspectionOrderService.get(id));
    }


    @ApiOperation(value = "确认点检工单结果", notes = "确认点检工单结果")
    @ApiImplicitParam(name = "inspectionOrderIds", value = "工单IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> inspectionOrderIds) {
        log.debug("name:{}", inspectionOrderIds);

        inspectionOrderService.batchConfirm(inspectionOrderIds);
        return Result.success();
    }

    @ApiOperation(value = "根据设备编码查询点检工单", notes = "根据设备编码查询点检工单")
    @ApiImplicitParam(name = "mchCode", value = "设备编码", required = true, dataType = "String")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findOrderByMchCode")
    public Result findOrderByMchCode(String mchCode) {
        log.debug("query with name:{}", mchCode);
        return Result.success(inspectionOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "修改点检工单", notes = "修改指定点检工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点检工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "点检工单实体", required = true, dataType = "InspectionOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody InspectionOrderForm inspectionOrderForm) {
        InspectionOrder inspectionOrder = inspectionOrderForm.toPo(id, InspectionOrder.class);
        return Result.success(inspectionOrderService.submitOrder(inspectionOrder));
    }

}