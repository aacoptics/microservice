package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.form.RepairOrderForm;
import com.aacoptics.wlg.equipment.entity.form.RepairOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.param.RepairOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.po.RepairOrder;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.RepairOrderService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repairOrder")
@Api("repairOrder")
@Slf4j
public class RepairOrderController {

    @Autowired
    RepairOrderService repairOrderService;

    @Autowired
    DataDictProvider dataDictProvider;


    @ApiOperation(value = "搜索维修工单", notes = "根据条件搜索维修工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "维修工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody RepairOrderQueryForm repairOrderQueryForm) {
        log.debug("query with name:{}", repairOrderQueryForm);
        return Result.success(repairOrderService.query(repairOrderQueryForm.getPage(), repairOrderQueryForm.toParam(RepairOrderQueryParam.class)));
    }

    @ApiOperation(value = "新增维修工单", notes = "新增一个维修工单信息")
    @ApiImplicitParam(name = "RepairForm", value = "新增维修工单form表单", required = true, dataType = "RepairOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody RepairOrderForm repairOrderForm) {
        log.debug("name:{}", repairOrderForm);
        RepairOrder repairOrder = repairOrderForm.toPo(RepairOrder.class);
        return Result.success(repairOrderService.add(repairOrder));
    }

    @ApiOperation(value = "删除维修工单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "维修工单ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(repairOrderService.delete(id));
    }

    @ApiOperation(value = "修改维修工单", notes = "修改指定维修工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "维修工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "RepairForm", value = "维修工单实体", required = true, dataType = "RepairOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RepairOrderForm repairOrderForm) {
        RepairOrder repairOrder = repairOrderForm.toPo(id, RepairOrder.class);
        return Result.success(repairOrderService.update(repairOrder));
    }

    @ApiOperation(value = "获取维修工单", notes = "获取指定维修工单信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "维修工单ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(repairOrderService.get(id));
    }


    @ApiOperation(value = "确认维修工单结果", notes = "确认维修工单结果")
    @ApiImplicitParam(name = "repairOrderIds", value = "维修工单IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> repairOrderIds) {
        log.debug("name:{}", repairOrderIds);

        repairOrderService.batchConfirm(repairOrderIds);
        return Result.success();
    }


    @ApiOperation(value = "根据设备编码查询维修工单", notes = "根据设备编码查询维修工单")
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
        return Result.success(repairOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "修改维修工单", notes = "修改指定维修工单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "维修工单ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "RepairForm", value = "维修工单实体", required = true, dataType = "RepairOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody RepairOrderForm repairOrderForm) {
        RepairOrder repairOrder = repairOrderForm.toPo(id, RepairOrder.class);
        return Result.success(repairOrderService.submitOrder(repairOrder));
    }



    @ApiOperation(value = "导出维修工单Excel", notes = "导出维修工单Excel")
    @PostMapping(value = "/exportRepairOrderExcel")
    public void exportRepairOrderExcel(@Valid @RequestBody RepairOrderQueryForm repairOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", repairOrderQueryForm);
        List<RepairOrderVO> repairOrderVOList = repairOrderService.queryRepairOrderByCondition(repairOrderQueryForm.toParam(RepairOrderQueryParam.class));

        //获取数据字典值
        //工单状态
        Result orderStatusResult = dataDictProvider.getDataDictList(DataDictConstants.REPAIR_ORDER_STATUS);
        HashMap<String, String> orderStatusMap = new HashMap<String, String>();
        if(orderStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)orderStatusResult.getData();
            orderStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.REPAIR_ORDER_STATUS + "数据字典失败，" + orderStatusResult.getMsg());
        }
        //工单来源
        Result orderSourceResult = dataDictProvider.getDataDictList(DataDictConstants.REPAIR_ORDER_SOURCE);
        HashMap<String, String> orderSourceMap = new HashMap<String, String>();
        if(orderSourceResult.isSuccess())
        {
            List<HashMap<String, Object>> orderSourceDataDictList =  (List<HashMap<String, Object>>)orderSourceResult.getData();
            orderSourceMap = DataDictUtil.convertDataDictListToMap(orderSourceDataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.REPAIR_ORDER_SOURCE + "数据字典失败，" + orderStatusResult.getMsg());
        }
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("维修工单");

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
        titleRow.createCell(9).setCellValue("故障描述");
        titleRow.createCell(10).setCellValue("维修说明");
        titleRow.createCell(11).setCellValue("维修时间");
        titleRow.createCell(12).setCellValue("工单来源");
        titleRow.createCell(13).setCellValue("更新人");
        titleRow.createCell(14).setCellValue("更新时间");
        titleRow.createCell(15).setCellValue("创建人");
        titleRow.createCell(16).setCellValue("创建时间");

        try {
            if (repairOrderVOList != null && repairOrderVOList.size() > 0) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int rowNumber = 1;
                for (int i = 0; i < repairOrderVOList.size(); i++) {
                    RepairOrderVO repairOrderVO = repairOrderVOList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(repairOrderVO.getOrderNumber() != null ? repairOrderVO.getOrderNumber() + "" : "");
                    dataRow.createCell(2).setCellValue(repairOrderVO.getMchCode() != null ? repairOrderVO.getMchCode() + "" : "");
                    dataRow.createCell(3).setCellValue(repairOrderVO.getMchName() != null ? repairOrderVO.getMchName() + "" : "");
                    dataRow.createCell(4).setCellValue(repairOrderVO.getSpec() != null ? repairOrderVO.getSpec() + "" : "");
                    dataRow.createCell(5).setCellValue(repairOrderVO.getTypeVersion() != null ? repairOrderVO.getTypeVersion() + "" : "");
                    dataRow.createCell(6).setCellValue(repairOrderVO.getFactoryNo() != null ? repairOrderVO.getFactoryNo() + "" : "");
                    dataRow.createCell(7).setCellValue(repairOrderVO.getDutyPersonId() != null ? repairOrderVO.getDutyPersonId() + "" : "");
                    //状态通过数据字典翻译
                    String orderStatus = repairOrderVO.getStatus() != null ? repairOrderVO.getStatus() + "" : "";
                    if(StringUtils.isNotEmpty(orderStatus))
                    {
                        if(orderStatusMap.containsKey(orderStatus))
                        {
                            orderStatus = orderStatusMap.get(orderStatus);
                        }
                    }
                    dataRow.createCell(8).setCellValue(orderStatus);
                    dataRow.createCell(9).setCellValue(repairOrderVO.getFaultDesc() != null ? repairOrderVO.getFaultDesc() + "" : "");
                    dataRow.createCell(10).setCellValue(repairOrderVO.getRepairDesc() != null ? repairOrderVO.getRepairDesc() + "" : "");
                    dataRow.createCell(11).setCellValue(repairOrderVO.getRepairDatetime() != null ? repairOrderVO.getRepairDatetime().format(dateTimeFormatter) : "");
                    //来源通过数据字典翻译
                    String sourceType = repairOrderVO.getSourceType() != null ? repairOrderVO.getSourceType() + "" : "";
                    if(StringUtils.isNotEmpty(sourceType))
                    {
                        if(orderSourceMap.containsKey(sourceType))
                        {
                            sourceType = orderSourceMap.get(sourceType);
                        }
                    }
                    dataRow.createCell(12).setCellValue(sourceType);
                    dataRow.createCell(13).setCellValue(repairOrderVO.getUpdatedBy() != null ? repairOrderVO.getUpdatedBy() + "" : "");
                    dataRow.createCell(14).setCellValue(repairOrderVO.getUpdatedTime() != null ? repairOrderVO.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                    dataRow.createCell(15).setCellValue(repairOrderVO.getCreatedBy() != null ? repairOrderVO.getCreatedBy() + "" : "");
                    dataRow.createCell(16).setCellValue(repairOrderVO.getCreatedTime() != null ? repairOrderVO.getCreatedTime().format(dateTimeFormatter) + "" : "");
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15,
                    256*15, 256*20, 256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("导出维修工单异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "维修工单.xlsx");
    }
}