package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.form.EquipmentQueryForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionOrderForm;
import com.aacoptics.wlg.equipment.entity.form.InspectionOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.EquipmentQueryParam;
import com.aacoptics.wlg.equipment.entity.param.InspectionOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.Equipment;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.entity.vo.InspectionOrderAndItemVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
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




    @ApiOperation(value = "导出点检工单Excel", notes = "导出点检工单Excel")
    @PostMapping(value = "/exportInspectionOrderExcel")
    public void exportInspectionOrderExcel(@Valid @RequestBody InspectionOrderQueryForm inspectionOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", inspectionOrderQueryForm);
        List<InspectionOrderAndItemVO> inspectionOrderAndItemVOList = inspectionOrderService.queryInspectionOrderByCondition(inspectionOrderQueryForm.toParam(InspectionOrderQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("点检工单");

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
        titleRow.createCell(9).setCellValue("点检日期");
        titleRow.createCell(10).setCellValue("点检班次");
        titleRow.createCell(11).setCellValue("班次开始时间");
        titleRow.createCell(12).setCellValue("班次结束时间");
        titleRow.createCell(13).setCellValue("点检项");
        titleRow.createCell(14).setCellValue("点检项判断标准");
        titleRow.createCell(15).setCellValue("起始范围值");
        titleRow.createCell(16).setCellValue("截至范围值");
        titleRow.createCell(17).setCellValue("实际值");
        titleRow.createCell(18).setCellValue("是否完成");
        titleRow.createCell(19).setCellValue("点检结果");
        titleRow.createCell(20).setCellValue("是否存在异常");
        titleRow.createCell(21).setCellValue("是否存在故障");
        titleRow.createCell(22).setCellValue("是否需要维修");
        titleRow.createCell(23).setCellValue("故障描述");
        titleRow.createCell(24).setCellValue("更新人");
        titleRow.createCell(25).setCellValue("更新时间");
        titleRow.createCell(26).setCellValue("创建人");
        titleRow.createCell(27).setCellValue("创建时间");

        try {
            if (inspectionOrderAndItemVOList != null && inspectionOrderAndItemVOList.size() > 0) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int rowNumber = 1;
                for (int i = 0; i < inspectionOrderAndItemVOList.size(); i++) {
                    InspectionOrderAndItemVO inspectionOrderAndItemVO = inspectionOrderAndItemVOList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(inspectionOrderAndItemVO.getOrderNumber() != null ? inspectionOrderAndItemVO.getOrderNumber() + "" : "");
                    dataRow.createCell(2).setCellValue(inspectionOrderAndItemVO.getMchCode() != null ? inspectionOrderAndItemVO.getMchCode() + "" : "");
                    dataRow.createCell(3).setCellValue(inspectionOrderAndItemVO.getMchName() != null ? inspectionOrderAndItemVO.getMchName() + "" : "");
                    dataRow.createCell(4).setCellValue(inspectionOrderAndItemVO.getSpec() != null ? inspectionOrderAndItemVO.getSpec() + "" : "");
                    dataRow.createCell(5).setCellValue(inspectionOrderAndItemVO.getTypeVersion() != null ? inspectionOrderAndItemVO.getTypeVersion() + "" : "");
                    dataRow.createCell(6).setCellValue(inspectionOrderAndItemVO.getFactoryNo() != null ? inspectionOrderAndItemVO.getFactoryNo() + "" : "");
                    dataRow.createCell(7).setCellValue(inspectionOrderAndItemVO.getDutyPersonId() != null ? inspectionOrderAndItemVO.getDutyPersonId() + "" : "");
                    dataRow.createCell(8).setCellValue(inspectionOrderAndItemVO.getStatus() != null ? inspectionOrderAndItemVO.getStatus() + "" : "");
                    dataRow.createCell(9).setCellValue(inspectionOrderAndItemVO.getInspectionDate() != null ? inspectionOrderAndItemVO.getInspectionDate().format(dateFormatter) + "" : "");
                    dataRow.createCell(10).setCellValue(inspectionOrderAndItemVO.getInspectionShift() != null ? inspectionOrderAndItemVO.getInspectionShift() + "" : "");
                    dataRow.createCell(11).setCellValue(inspectionOrderAndItemVO.getShiftStartTime() != null ? inspectionOrderAndItemVO.getShiftStartTime().format(dateTimeFormatter) + "" : "");
                    dataRow.createCell(12).setCellValue(inspectionOrderAndItemVO.getShiftEndTime() != null ? inspectionOrderAndItemVO.getShiftEndTime().format(dateTimeFormatter) + "" : "");

                    List<InspectionOrderItem> inspectionOrderItemList = inspectionOrderAndItemVO.getInspectionOrderItemList();

                    for(int j=0; j<inspectionOrderItemList.size(); j++)
                    {
                        InspectionOrderItem inspectionOrderItem = inspectionOrderItemList.get(j);
                        if(j != 0)
                        {
                            dataRow = wbSheet.createRow(rowNumber++);
                            dataRow.createCell(0).setCellValue(rowNumber - 1);
                        }
                        dataRow.createCell(13).setCellValue(inspectionOrderItem.getCheckItem() != null ? inspectionOrderItem.getCheckItem() + "" : "");
                        dataRow.createCell(14).setCellValue(inspectionOrderItem.getCheckItemStandard() != null ? inspectionOrderItem.getCheckItemStandard() + "" : "");
                        if(inspectionOrderItem.getMinValue() != null) {
                            dataRow.createCell(15).setCellValue(Double.valueOf(inspectionOrderItem.getMinValue() + ""));
                        }else{
                            dataRow.createCell(15).setCellType(CellType.BLANK);
                        }
                        if(inspectionOrderItem.getMaxValue() != null) {
                            dataRow.createCell(16).setCellValue(Double.valueOf(inspectionOrderItem.getMaxValue() + ""));
                        }else{
                            dataRow.createCell(16).setCellType(CellType.BLANK);
                        }
                        if(inspectionOrderItem.getActualValue() != null) {
                            dataRow.createCell(17).setCellValue(Double.valueOf(inspectionOrderItem.getActualValue() + ""));
                        }else{
                            dataRow.createCell(17).setCellType(CellType.BLANK);
                        }
                        dataRow.createCell(18).setCellValue(inspectionOrderItem.getIsFinish() != null ? inspectionOrderItem.getIsFinish() + "" : "");
                        dataRow.createCell(19).setCellValue(inspectionOrderItem.getCheckResult() != null ? inspectionOrderItem.getCheckResult() + "" : "");
                        dataRow.createCell(20).setCellValue(inspectionOrderItem.getIsException() != null ? inspectionOrderItem.getIsException() + "" : "");
                        dataRow.createCell(21).setCellValue(inspectionOrderItem.getIsFault() != null ? inspectionOrderItem.getIsFault() + "" : "");
                        dataRow.createCell(22).setCellValue(inspectionOrderItem.getIsRepair() != null ? inspectionOrderItem.getIsRepair() + "" : "");
                        dataRow.createCell(23).setCellValue(inspectionOrderItem.getFaultDesc() != null ? inspectionOrderItem.getFaultDesc() + "" : "");
                        dataRow.createCell(24).setCellValue(inspectionOrderItem.getUpdatedBy() != null ? inspectionOrderItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(25).setCellValue(inspectionOrderItem.getUpdatedTime() != null ? inspectionOrderItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(26).setCellValue(inspectionOrderItem.getCreatedBy() != null ? inspectionOrderItem.getCreatedBy() + "" : "");
                        dataRow.createCell(27).setCellValue(inspectionOrderItem.getCreatedTime() != null ? inspectionOrderItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //合并主表单元格
                    for(int k=1; k<=12; k++) {
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-inspectionOrderItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15,
                                                              256*10, 256*20, 256*20, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,
                                                              256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("导出点检工单异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "点检工单.xlsx");
    }

}