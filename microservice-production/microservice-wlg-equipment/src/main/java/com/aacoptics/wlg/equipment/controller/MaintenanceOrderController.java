package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.InspectionOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/maintenanceOrder")
@Api("maintenanceOrder")
@Slf4j
public class MaintenanceOrderController {

    @Autowired
    MaintenanceOrderService maintenanceOrderService;

    @Autowired
    MaintenanceItemService maintenanceItemService;


    @ApiOperation(value = "搜索保养工单", notes = "根据条件搜索保养工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "保养工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm) {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        return Result.success(maintenanceOrderService.query(maintenanceOrderQueryForm.getPage(), maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class)));
    }

    @ApiOperation(value = "新增保养工单", notes = "新增一个保养工单信息")
    @ApiImplicitParam(name = "MaintenanceForm", value = "新增保养工单form表单", required = true, dataType = "MaintenanceOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        log.debug("name:{}", maintenanceOrderForm);
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.add(maintenanceOrder));
    }

    @ApiOperation(value = "删除保养工单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养工单ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(maintenanceOrderService.delete(id));
    }

    @ApiOperation(value = "修改保养工单", notes = "修改指定保养工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "保养工单实体", required = true, dataType = "MaintenanceOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(id, MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.update(maintenanceOrder));
    }

    @ApiOperation(value = "获取保养工单", notes = "获取指定保养工单信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "保养工单ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(maintenanceOrderService.get(id));
    }

    @ApiOperation(value = "确认保养工单结果", notes = "确认保养工单结果")
    @ApiImplicitParam(name = "maintenanceOrderIds", value = "工单IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> maintenanceOrderIds) {
        log.debug("name:{}", maintenanceOrderIds);

        maintenanceOrderService.batchConfirm(maintenanceOrderIds);
        return Result.success();
    }

    @ApiOperation(value = "根据设备编码查询保养工单", notes = "根据设备编码查询保养工单")
    @ApiImplicitParam(name = "mchCode", value = "设备编码", required = true, dataType = "String")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findOrderByMchCode")
    public Result findOrderByMchCode(@RequestBody String requestBody) {
        log.debug("query with name:{}", requestBody);
        if(StringUtils.isEmpty(requestBody))
        {
            throw new BusinessException("参数不能为空");
        }
        JSONObject jsonObject = JSON.parseObject(requestBody);
        String mchCode = jsonObject.getString("mchCode");
        if(StringUtils.isEmpty(mchCode))
        {
            throw new BusinessException("设备编码不能为空");
        }
        return Result.success(maintenanceOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "修改保养工单", notes = "修改指定保养工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "保养工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "保养工单实体", required = true, dataType = "MaintenanceOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(id, MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.submitOrder(maintenanceOrder));
    }




    @ApiOperation(value = "导出保养工单Excel", notes = "导出保养工单Excel")
    @PostMapping(value = "/exportMaintenanceOrderExcel")
    public void exportMaintenanceOrderExcel(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        List<MaintenanceOrderAndItemVO> maintenanceOrderAndItemVOList = maintenanceOrderService.queryMaintenanceOrderByCondition(maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("保养工单");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("工单号");
        titleRow.createCell(2).setCellValue("设备编码");
        titleRow.createCell(3).setCellValue("设备名称");
        titleRow.createCell(4).setCellValue("规格");
        titleRow.createCell(5).setCellValue("型号");
        titleRow.createCell(6).setCellValue("出厂编码");
        titleRow.createCell(7).setCellValue("责任人");
        titleRow.createCell(8).setCellValue("状态");
        titleRow.createCell(9).setCellValue("保养日期");
        titleRow.createCell(10).setCellValue("保养项");
        titleRow.createCell(11).setCellValue("保养项判断标准");
        titleRow.createCell(12).setCellValue("起始范围值");
        titleRow.createCell(13).setCellValue("截至范围值");
        titleRow.createCell(14).setCellValue("实际值");
        titleRow.createCell(15).setCellValue("是否完成");
        titleRow.createCell(16).setCellValue("保养结果");
        titleRow.createCell(17).setCellValue("是否存在异常");
        titleRow.createCell(18).setCellValue("是否存在故障");
        titleRow.createCell(19).setCellValue("是否需要维修");
        titleRow.createCell(20).setCellValue("故障描述");
        titleRow.createCell(21).setCellValue("更新人");
        titleRow.createCell(22).setCellValue("更新时间");
        titleRow.createCell(23).setCellValue("创建人");
        titleRow.createCell(24).setCellValue("创建时间");

        try {
            if (maintenanceOrderAndItemVOList != null && maintenanceOrderAndItemVOList.size() > 0) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int rowNumber = 1;
                for (int i = 0; i < maintenanceOrderAndItemVOList.size(); i++) {
                    MaintenanceOrderAndItemVO maintenanceOrderAndItemVO = maintenanceOrderAndItemVOList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(maintenanceOrderAndItemVO.getOrderNumber() != null ? maintenanceOrderAndItemVO.getOrderNumber() + "" : "");
                    dataRow.createCell(2).setCellValue(maintenanceOrderAndItemVO.getMchCode() != null ? maintenanceOrderAndItemVO.getMchCode() + "" : "");
                    dataRow.createCell(3).setCellValue(maintenanceOrderAndItemVO.getMchName() != null ? maintenanceOrderAndItemVO.getMchName() + "" : "");
                    dataRow.createCell(4).setCellValue(maintenanceOrderAndItemVO.getSpec() != null ? maintenanceOrderAndItemVO.getSpec() + "" : "");
                    dataRow.createCell(5).setCellValue(maintenanceOrderAndItemVO.getTypeVersion() != null ? maintenanceOrderAndItemVO.getTypeVersion() + "" : "");
                    dataRow.createCell(6).setCellValue(maintenanceOrderAndItemVO.getFactoryNo() != null ? maintenanceOrderAndItemVO.getFactoryNo() + "" : "");
                    dataRow.createCell(7).setCellValue(maintenanceOrderAndItemVO.getDutyPersonId() != null ? maintenanceOrderAndItemVO.getDutyPersonId() + "" : "");
                    dataRow.createCell(8).setCellValue(maintenanceOrderAndItemVO.getStatus() != null ? maintenanceOrderAndItemVO.getStatus() + "" : "");
                    dataRow.createCell(9).setCellValue(maintenanceOrderAndItemVO.getMaintenanceDate() != null ? maintenanceOrderAndItemVO.getMaintenanceDate().format(dateFormatter) + "" : "");

                    List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrderAndItemVO.getMaintenanceOrderItemList();

                    for(int j=0; j<maintenanceOrderItemList.size(); j++)
                    {
                        MaintenanceOrderItem maintenanceOrderItem = maintenanceOrderItemList.get(j);
                        if(j != 0)
                        {
                            dataRow = wbSheet.createRow(rowNumber++);
                            dataRow.createCell(0).setCellValue(rowNumber - 1);
                        }
                        dataRow.createCell(10).setCellValue(maintenanceOrderItem.getMaintenanceItem() != null ? maintenanceOrderItem.getMaintenanceItem() + "" : "");
                        dataRow.createCell(11).setCellValue(maintenanceOrderItem.getMaintenanceItemStandard() != null ? maintenanceOrderItem.getMaintenanceItemStandard() + "" : "");
                        if(maintenanceOrderItem.getMinValue() != null) {
                            dataRow.createCell(12).setCellValue(Double.valueOf(maintenanceOrderItem.getMinValue() + ""));
                        }else{
                            dataRow.createCell(12).setCellType(CellType.BLANK);
                        }
                        if(maintenanceOrderItem.getMaxValue() != null) {
                            dataRow.createCell(13).setCellValue(Double.valueOf(maintenanceOrderItem.getMaxValue() + ""));
                        }else{
                            dataRow.createCell(13).setCellType(CellType.BLANK);
                        }
                        if(maintenanceOrderItem.getActualValue() != null) {
                            dataRow.createCell(14).setCellValue(Double.valueOf(maintenanceOrderItem.getActualValue() + ""));
                        }else{
                            dataRow.createCell(14).setCellType(CellType.BLANK);
                        }
                        dataRow.createCell(15).setCellValue(maintenanceOrderItem.getIsFinish() != null ? maintenanceOrderItem.getIsFinish() + "" : "");
                        dataRow.createCell(16).setCellValue(maintenanceOrderItem.getMaintenanceResult() != null ? maintenanceOrderItem.getMaintenanceResult() + "" : "");
                        dataRow.createCell(17).setCellValue(maintenanceOrderItem.getIsException() != null ? maintenanceOrderItem.getIsException() + "" : "");
                        dataRow.createCell(18).setCellValue(maintenanceOrderItem.getIsFault() != null ? maintenanceOrderItem.getIsFault() + "" : "");
                        dataRow.createCell(19).setCellValue(maintenanceOrderItem.getIsRepair() != null ? maintenanceOrderItem.getIsRepair() + "" : "");
                        dataRow.createCell(20).setCellValue(maintenanceOrderItem.getFaultDesc() != null ? maintenanceOrderItem.getFaultDesc() + "" : "");
                        dataRow.createCell(21).setCellValue(maintenanceOrderItem.getUpdatedBy() != null ? maintenanceOrderItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(22).setCellValue(maintenanceOrderItem.getUpdatedTime() != null ? maintenanceOrderItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(23).setCellValue(maintenanceOrderItem.getCreatedBy() != null ? maintenanceOrderItem.getCreatedBy() + "" : "");
                        dataRow.createCell(24).setCellValue(maintenanceOrderItem.getCreatedTime() != null ? maintenanceOrderItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //合并主表单元格
                    for(int k=1; k<=9; k++) {
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-maintenanceOrderItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15,
                    256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,
                    256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("导出保养工单异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "保养工单.xlsx");
    }

}