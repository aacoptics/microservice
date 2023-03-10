package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.constant.InspectionOrderStatusConstants;
import com.aacoptics.wlg.equipment.constant.MaintenanceOrderStatusConstants;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrder;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.MaintenanceItemService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
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

@RestController
@RequestMapping("/maintenanceOrder")
@Api("maintenanceOrder")
@Slf4j
public class MaintenanceOrderController {

    @Autowired
    MaintenanceOrderService maintenanceOrderService;

    @Autowired
    MaintenanceItemService maintenanceItemService;

    @Autowired
    DataDictProvider dataDictProvider;


    @ApiOperation(value = "??????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "????????????????????????", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm) {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        return Result.success(maintenanceOrderService.query(maintenanceOrderQueryForm.getPage(), maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class)));
    }

    @ApiOperation(value = "??????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "????????????????????????", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/queryDetail")
    public Result queryDetail(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm) {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        return Result.success(maintenanceOrderService.queryDetail(maintenanceOrderQueryForm.getPage(), maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class)));
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(name = "MaintenanceForm", value = "??????????????????form??????", required = true, dataType = "MaintenanceOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        log.debug("name:{}", maintenanceOrderForm);
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.add(maintenanceOrder));
    }

    @ApiOperation(value = "??????????????????", notes = "??????url???id?????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(maintenanceOrderService.delete(id));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "??????????????????", required = true, dataType = "MaintenanceOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {

        MaintenanceOrder maintenanceOrderTarget = maintenanceOrderService.get(id);
        if(MaintenanceOrderStatusConstants.COMMITTED.equals(maintenanceOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }
        if(MaintenanceOrderStatusConstants.CONFIRMED.equals(maintenanceOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }


        maintenanceOrderTarget.setDutyPersonId(maintenanceOrderForm.getDutyPersonId() != null ? maintenanceOrderForm.getDutyPersonId().trim() : null);

        return Result.success(maintenanceOrderService.update(maintenanceOrderTarget));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(maintenanceOrderService.get(id));
    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "maintenanceOrderIds", value = "??????IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> maintenanceOrderIds) {
        log.debug("name:{}", maintenanceOrderIds);

        maintenanceOrderService.batchConfirm(maintenanceOrderIds);
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
        return Result.success(maintenanceOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????")
    @ApiImplicitParam(name = "requestBody", value = "??????", required = true, dataType = "String")
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
        return Result.success(maintenanceOrderService.findOrderByUser(user));
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "MaintenanceForm", value = "??????????????????", required = true, dataType = "MaintenanceOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderForm maintenanceOrderForm) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderForm.toPo(id, MaintenanceOrder.class);
        return Result.success(maintenanceOrderService.submitOrder(maintenanceOrder));
    }




    @ApiOperation(value = "??????????????????Excel", notes = "??????????????????Excel")
    @PostMapping(value = "/exportMaintenanceOrderExcel")
    public void exportMaintenanceOrderExcel(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        List<MaintenanceOrderAndItemVO> maintenanceOrderAndItemVOList = maintenanceOrderService.queryMaintenanceOrderByCondition(maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class));

        //?????????????????????
        //????????????
        Result orderStatusResult = dataDictProvider.getDataDictList(DataDictConstants.MAINTENANCE_ORDER_STATUS);
        HashMap<String, String> orderStatusMap = new HashMap<String, String>();
        if(orderStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)orderStatusResult.getData();
            orderStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.MAINTENANCE_ORDER_STATUS + "?????????????????????" + orderStatusResult.getMsg());
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
            log.error("??????" + DataDictConstants.YES_NO + "?????????????????????" + yesNoResult.getMsg());
        }
        //???????????????
        Result itemTypeResult = dataDictProvider.getDataDictList(DataDictConstants.ITEM_TYPE);
        HashMap<String, String> itemTypeMap = new HashMap<String, String>();
        if(itemTypeResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)itemTypeResult.getData();
            itemTypeMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.ITEM_TYPE + "?????????????????????" + itemTypeResult.getMsg());
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
        titleRow.createCell(10).setCellValue("?????????");
        titleRow.createCell(11).setCellValue("???????????????");
        titleRow.createCell(12).setCellValue("???????????????");
        titleRow.createCell(13).setCellValue("???????????????");
        titleRow.createCell(14).setCellValue("?????????????????????");
        titleRow.createCell(15).setCellValue("?????????");
        titleRow.createCell(16).setCellValue("?????????");
        titleRow.createCell(17).setCellValue("????????????");
        titleRow.createCell(18).setCellValue("????????????");
        titleRow.createCell(19).setCellValue("??????????????????");
        titleRow.createCell(20).setCellValue("??????????????????");
//        titleRow.createCell(21).setCellValue("??????????????????");
        titleRow.createCell(22).setCellValue("????????????");
        titleRow.createCell(23).setCellValue("?????????");
        titleRow.createCell(24).setCellValue("????????????");
        titleRow.createCell(25).setCellValue("?????????");
        titleRow.createCell(26).setCellValue("????????????");

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

                    //??????????????????????????????
                    String orderStatus = maintenanceOrderAndItemVO.getStatus() != null ? maintenanceOrderAndItemVO.getStatus() + "" : "";
                    if(StringUtils.isNotEmpty(orderStatus))
                    {
                        if(orderStatusMap.containsKey(orderStatus))
                        {
                            orderStatus = orderStatusMap.get(orderStatus);
                        }
                    }
                    dataRow.createCell(8).setCellValue(orderStatus);
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

                        //???????????????????????????????????????
                        String itemType = maintenanceOrderItem.getItemType() != null ? maintenanceOrderItem.getItemType() + "" : "";
                        if(StringUtils.isNotEmpty(itemType))
                        {
                            if(itemTypeMap.containsKey(itemType))
                            {
                                itemType = itemTypeMap.get(itemType);
                            }
                        }
                        dataRow.createCell(11).setCellValue(itemType);

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
                        dataRow.createCell(14).setCellValue(maintenanceOrderItem.getMaintenanceItemStandard() != null ? maintenanceOrderItem.getMaintenanceItemStandard() + "" : "");

                        dataRow.createCell(15).setCellValue(maintenanceOrderItem.getTheoreticalValue() != null ? maintenanceOrderItem.getTheoreticalValue() + "" : "");

                        if(maintenanceOrderItem.getActualValue() != null) {
                            dataRow.createCell(16).setCellValue(Double.valueOf(maintenanceOrderItem.getActualValue() + ""));
                        }else{
                            dataRow.createCell(16).setCellType(CellType.BLANK);
                        }

                        String isFinish = maintenanceOrderItem.getIsFinish() != null ? maintenanceOrderItem.getIsFinish() + "" : "";
                        if(StringUtils.isNotEmpty(isFinish))
                        {
                            if(yesNoMap.containsKey(isFinish))
                            {
                                isFinish = yesNoMap.get(isFinish);
                            }
                        }
                        dataRow.createCell(17).setCellValue(isFinish);
                        dataRow.createCell(18).setCellValue(maintenanceOrderItem.getMaintenanceResult() != null ? maintenanceOrderItem.getMaintenanceResult() + "" : "");

                        String isException = maintenanceOrderItem.getIsException() != null ? maintenanceOrderItem.getIsException() + "" : "";
                        if(StringUtils.isNotEmpty(isException))
                        {
                            if(yesNoMap.containsKey(isException))
                            {
                                isException = yesNoMap.get(isException);
                            }
                        }
                        dataRow.createCell(19).setCellValue(isException);

                        String isFault = maintenanceOrderItem.getIsFault() != null ? maintenanceOrderItem.getIsFault() + "" : "";
                        if(StringUtils.isNotEmpty(isFault))
                        {
                            if(yesNoMap.containsKey(isFault))
                            {
                                isFault = yesNoMap.get(isFault);
                            }
                        }
                        dataRow.createCell(20).setCellValue(isFault);

//                        String isRepair = maintenanceOrderItem.getIsRepair() != null ? maintenanceOrderItem.getIsRepair() + "" : "";
//                        if(StringUtils.isNotEmpty(isRepair))
//                        {
//                            if(yesNoMap.containsKey(isRepair))
//                            {
//                                isRepair = yesNoMap.get(isRepair);
//                            }
//                        }
//                        dataRow.createCell(21).setCellValue(isRepair);
                        dataRow.createCell(21).setCellValue(maintenanceOrderItem.getFaultDesc() != null ? maintenanceOrderItem.getFaultDesc() + "" : "");
                        dataRow.createCell(22).setCellValue(maintenanceOrderItem.getUpdatedBy() != null ? maintenanceOrderItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(23).setCellValue(maintenanceOrderItem.getUpdatedTime() != null ? maintenanceOrderItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(24).setCellValue(maintenanceOrderItem.getCreatedBy() != null ? maintenanceOrderItem.getCreatedBy() + "" : "");
                        dataRow.createCell(25).setCellValue(maintenanceOrderItem.getCreatedTime() != null ? maintenanceOrderItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //?????????????????????
                    for(int k=1; k<=9; k++) {
                        if(maintenanceOrderItemList.size() <= 1)
                        {
                            continue;
                        }
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-maintenanceOrderItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15,
                    256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,
                    256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("????????????????????????", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "????????????.xlsx");
    }

}