package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.constant.MaintenanceOrderStatusConstants;
import com.aacoptics.wlg.equipment.constant.RepairOrderStatusConstants;
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


    @ApiOperation(value = "??????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "????????????????????????", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody RepairOrderQueryForm repairOrderQueryForm) {
        log.debug("query with name:{}", repairOrderQueryForm);
        return Result.success(repairOrderService.query(repairOrderQueryForm.getPage(), repairOrderQueryForm.toParam(RepairOrderQueryParam.class)));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(name = "RepairForm", value = "??????????????????form??????", required = true, dataType = "RepairOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody RepairOrderForm repairOrderForm) {
        log.debug("name:{}", repairOrderForm);
        RepairOrder repairOrder = repairOrderForm.toPo(RepairOrder.class);
        return Result.success(repairOrderService.add(repairOrder));
    }

    @ApiOperation(value = "??????????????????", notes = "??????url???id?????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(repairOrderService.delete(id));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "RepairForm", value = "??????????????????", required = true, dataType = "RepairOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RepairOrderForm repairOrderForm) {

        RepairOrder repairOrderTarget = repairOrderService.get(id);
        if(RepairOrderStatusConstants.REPAIRED.equals(repairOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }
        if(RepairOrderStatusConstants.CONFIRMED.equals(repairOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }

        repairOrderTarget.setDutyPersonId(repairOrderForm.getDutyPersonId() != null ? repairOrderForm.getDutyPersonId().trim() : null);

        return Result.success(repairOrderService.update(repairOrderTarget));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(repairOrderService.get(id));
    }


    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "repairOrderIds", value = "????????????IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> repairOrderIds) {
        log.debug("name:{}", repairOrderIds);

        repairOrderService.batchConfirm(repairOrderIds);
        return Result.success();
    }


    @ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "mchCode", value = "????????????", required = true, dataType = "String")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/findOrderByMchCode")
    public Result findOrderByMchCode(@RequestBody String requestBody) {
        log.debug("query with name:{}", requestBody);
        if(StringUtils.isEmpty(requestBody))
        {
            throw new BusinessException("??????????????????");
        }
        JSONObject jsonObject = JSON.parseObject(requestBody);
        String mchCode = jsonObject.getString("mchCode");
        if(StringUtils.isEmpty(mchCode))
        {
            throw new BusinessException("????????????????????????");
        }
        return Result.success(repairOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(name = "user", value = "??????", required = true, dataType = "String")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/findOrderByUser")
    public Result findOrderByUser(@RequestBody String requestBody) {
        log.debug("query with name:{}", requestBody);
        String user = "";
        if(StringUtils.isNotEmpty(requestBody)) {
            JSONObject jsonObject = JSON.parseObject(requestBody);
            user = jsonObject.getString("user");
        }
        return Result.success(repairOrderService.findOrderByUser(user));
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "RepairForm", value = "??????????????????", required = true, dataType = "RepairOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody RepairOrderForm repairOrderForm) {
        RepairOrder repairOrder = repairOrderForm.toPo(id, RepairOrder.class);
        return Result.success(repairOrderService.submitOrder(repairOrder));
    }



    @ApiOperation(value = "??????????????????Excel", notes = "??????????????????Excel")
    @PostMapping(value = "/exportRepairOrderExcel")
    public void exportRepairOrderExcel(@Valid @RequestBody RepairOrderQueryForm repairOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", repairOrderQueryForm);
        List<RepairOrderVO> repairOrderVOList = repairOrderService.queryRepairOrderByCondition(repairOrderQueryForm.toParam(RepairOrderQueryParam.class));

        //?????????????????????
        //????????????
        Result orderStatusResult = dataDictProvider.getDataDictList(DataDictConstants.REPAIR_ORDER_STATUS);
        HashMap<String, String> orderStatusMap = new HashMap<String, String>();
        if(orderStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)orderStatusResult.getData();
            orderStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.REPAIR_ORDER_STATUS + "?????????????????????" + orderStatusResult.getMsg());
        }
        //????????????
        Result orderSourceResult = dataDictProvider.getDataDictList(DataDictConstants.REPAIR_ORDER_SOURCE);
        HashMap<String, String> orderSourceMap = new HashMap<String, String>();
        if(orderSourceResult.isSuccess())
        {
            List<HashMap<String, Object>> orderSourceDataDictList =  (List<HashMap<String, Object>>)orderSourceResult.getData();
            orderSourceMap = DataDictUtil.convertDataDictListToMap(orderSourceDataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.REPAIR_ORDER_SOURCE + "?????????????????????" + orderStatusResult.getMsg());
        }
        //??????
        Result yesNoResult = dataDictProvider.getDataDictList(DataDictConstants.YES_NO);
        HashMap<String, String> yesNoMap = new HashMap<String, String>();
        if(yesNoResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)yesNoResult.getData();
            yesNoMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.REPAIR_ORDER_STATUS + "?????????????????????" + orderStatusResult.getMsg());
        }


        //???????????????
        XSSFWorkbook workbook = new XSSFWorkbook();
        //???????????????
        XSSFSheet wbSheet = workbook.createSheet("????????????");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("??????");
        titleRow.createCell(1).setCellValue("?????????");
        titleRow.createCell(2).setCellValue("????????????");
        titleRow.createCell(3).setCellValue("????????????");
        titleRow.createCell(4).setCellValue("??????");
        titleRow.createCell(5).setCellValue("??????");
        titleRow.createCell(6).setCellValue("????????????");
        titleRow.createCell(7).setCellValue("?????????");
        titleRow.createCell(8).setCellValue("??????");
        titleRow.createCell(9).setCellValue("????????????");
        titleRow.createCell(10).setCellValue("????????????");
        titleRow.createCell(11).setCellValue("????????????");
        titleRow.createCell(12).setCellValue("??????");
        titleRow.createCell(13).setCellValue("????????????");
        titleRow.createCell(14).setCellValue("????????????");
        titleRow.createCell(15).setCellValue("????????????");
        titleRow.createCell(16).setCellValue("????????????");
        titleRow.createCell(17).setCellValue("??????");
        titleRow.createCell(18).setCellValue("????????????");
        titleRow.createCell(19).setCellValue("????????????");
        titleRow.createCell(20).setCellValue("???????????????Min???");
        titleRow.createCell(21).setCellValue("???????????????Min???");
        titleRow.createCell(22).setCellValue("???????????????Min???");
        titleRow.createCell(23).setCellValue("????????????");
        titleRow.createCell(24).setCellValue("?????????");
        titleRow.createCell(25).setCellValue("????????????");
        titleRow.createCell(26).setCellValue("?????????");
        titleRow.createCell(27).setCellValue("????????????");

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
                    //??????????????????????????????
                    String orderStatus = repairOrderVO.getStatus() != null ? repairOrderVO.getStatus() + "" : "";
                    if(StringUtils.isNotEmpty(orderStatus))
                    {
                        if(orderStatusMap.containsKey(orderStatus))
                        {
                            orderStatus = orderStatusMap.get(orderStatus);
                        }
                    }
                    dataRow.createCell(8).setCellValue(orderStatus);
                    dataRow.createCell(9).setCellValue(repairOrderVO.getExceptionType() != null ? repairOrderVO.getExceptionType() + "" : "");
                    dataRow.createCell(10).setCellValue(repairOrderVO.getExceptionSubclass() != null ? repairOrderVO.getExceptionSubclass() + "" : "");
                    dataRow.createCell(11).setCellValue(repairOrderVO.getFaultDesc() != null ? repairOrderVO.getFaultDesc() + "" : "");
                    dataRow.createCell(12).setCellValue(repairOrderVO.getMould() != null ? repairOrderVO.getMould() + "" : "");
                    dataRow.createCell(13).setCellValue(repairOrderVO.getReason() != null ? repairOrderVO.getReason() + "" : "");
                    dataRow.createCell(14).setCellValue(repairOrderVO.getHandleMethod() != null ? repairOrderVO.getHandleMethod() + "" : "");

                    //??????????????????????????????
                    String isClosed = repairOrderVO.getIsClosed() != null ? repairOrderVO.getIsClosed() + "" : "";
                    if(StringUtils.isNotEmpty(isClosed))
                    {
                        if(yesNoMap.containsKey(isClosed))
                        {
                            isClosed = yesNoMap.get(isClosed);
                        }
                    }
                    dataRow.createCell(15).setCellValue(isClosed);
                    dataRow.createCell(16).setCellValue(repairOrderVO.getLongTermMeasure() != null ? repairOrderVO.getLongTermMeasure() + "" : "");
                    dataRow.createCell(17).setCellValue(repairOrderVO.getRepairDesc() != null ? repairOrderVO.getRepairDesc() + "" : "");
                    dataRow.createCell(18).setCellValue(repairOrderVO.getStageDatetime() != null ? repairOrderVO.getStageDatetime().format(dateTimeFormatter) : "");
                    dataRow.createCell(19).setCellValue(repairOrderVO.getRepairDatetime() != null ? repairOrderVO.getRepairDatetime().format(dateTimeFormatter) : "");

                    if(repairOrderVO.getReceiveOrderTime() != null) {
                        dataRow.createCell(20).setCellValue(Double.valueOf(repairOrderVO.getReceiveOrderTime() + ""));
                    }else{
                        dataRow.createCell(20).setCellType(CellType.BLANK);
                    }
                    if(repairOrderVO.getRepairOrderTime() != null) {
                        dataRow.createCell(21).setCellValue(Double.valueOf(repairOrderVO.getRepairOrderTime() + ""));
                    }else{
                        dataRow.createCell(21).setCellType(CellType.BLANK);
                    }
                    if(repairOrderVO.getConsumptionTime() != null) {
                        dataRow.createCell(22).setCellValue(Double.valueOf(repairOrderVO.getConsumptionTime() + ""));
                    }else{
                        dataRow.createCell(22).setCellType(CellType.BLANK);
                    }

                    //??????????????????????????????
                    String sourceType = repairOrderVO.getSourceType() != null ? repairOrderVO.getSourceType() + "" : "";
                    if(StringUtils.isNotEmpty(sourceType))
                    {
                        if(orderSourceMap.containsKey(sourceType))
                        {
                            sourceType = orderSourceMap.get(sourceType);
                        }
                    }
                    dataRow.createCell(23).setCellValue(sourceType);
                    dataRow.createCell(24).setCellValue(repairOrderVO.getUpdatedBy() != null ? repairOrderVO.getUpdatedBy() + "" : "");
                    dataRow.createCell(25).setCellValue(repairOrderVO.getUpdatedTime() != null ? repairOrderVO.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                    dataRow.createCell(26).setCellValue(repairOrderVO.getCreatedBy() != null ? repairOrderVO.getCreatedBy() + "" : "");
                    dataRow.createCell(27).setCellValue(repairOrderVO.getCreatedTime() != null ? repairOrderVO.getCreatedTime().format(dateTimeFormatter) + "" : "");
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,256*15, 256*15,
                    256*15, 256*15, 256*20, 256*20, 256*20, 256*20, 256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("????????????????????????", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "????????????.xlsx");
    }
}