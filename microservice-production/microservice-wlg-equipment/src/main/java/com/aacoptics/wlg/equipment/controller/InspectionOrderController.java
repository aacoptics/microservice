package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.constant.InspectionOrderStatusConstants;
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
import com.aacoptics.wlg.equipment.provider.DataDictProvider;
import com.aacoptics.wlg.equipment.service.InspectionItemService;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.InspectionShiftService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
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
import java.util.HashMap;
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

    @Autowired
    DataDictProvider dataDictProvider;

    @ApiOperation(value = "??????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "????????????????????????", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result conditions(@Valid @RequestBody InspectionOrderQueryForm inspectionOrderQueryForm) {
        log.debug("query with name:{}", inspectionOrderQueryForm);
        return Result.success(inspectionOrderService.query(inspectionOrderQueryForm.getPage(), inspectionOrderQueryForm.toParam(InspectionOrderQueryParam.class)));
    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "????????????????????????", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "????????????", response = Result.class)
    )
    @PostMapping(value = "/queryDetail")
    public Result queryDetail(@Valid @RequestBody InspectionOrderQueryForm inspectionOrderQueryForm) {
        log.debug("query with name:{}", inspectionOrderQueryForm);
        return Result.success(inspectionOrderService.queryDetail(inspectionOrderQueryForm.getPage(), inspectionOrderQueryForm.toParam(InspectionOrderQueryParam.class)));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(name = "InspectionForm", value = "??????????????????form??????", required = true, dataType = "InspectionOrderForm")
    @PostMapping
    public Result add(@Valid @RequestBody InspectionOrderForm inspectionOrderForm) {
        log.debug("name:{}", inspectionOrderForm);
        InspectionOrder inspectionOrder = inspectionOrderForm.toPo(InspectionOrder.class);
        return Result.success(inspectionOrderService.add(inspectionOrder));
    }

    @ApiOperation(value = "??????????????????", notes = "??????url???id?????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(inspectionOrderService.delete(id));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "??????????????????", required = true, dataType = "InspectionOrderForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody InspectionOrderForm inspectionOrderForm) {

        InspectionOrder inspectionOrderTarget = inspectionOrderService.get(id);
        if(InspectionOrderStatusConstants.COMMITTED.equals(inspectionOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }
        if(InspectionOrderStatusConstants.CONFIRMED.equals(inspectionOrderTarget.getStatus()))
        {
            throw new BusinessException("???????????????????????????????????????");
        }

        inspectionOrderTarget.setDutyPersonId(inspectionOrderForm.getDutyPersonId() != null ? inspectionOrderForm.getDutyPersonId().trim() : null);

        return Result.success(inspectionOrderService.update(inspectionOrderTarget));
    }

    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(paramType = "path", name = "id", value = "????????????ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        return Result.success(inspectionOrderService.get(id));
    }


    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "inspectionOrderIds", value = "??????IDS", required = true, dataType = "List")
    @PostMapping("/batchConfirm")
    public Result batchConfirm(@Valid @RequestBody List<String> inspectionOrderIds) {
        log.debug("name:{}", inspectionOrderIds);

        inspectionOrderService.batchConfirm(inspectionOrderIds);
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
        return Result.success(inspectionOrderService.findOrderByMchCode(mchCode));
    }

    @ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "mchCode", value = "????????????", required = true, dataType = "String")
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
        return Result.success(inspectionOrderService.findOrderByUser(user));
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "InspectionForm", value = "??????????????????", required = true, dataType = "InspectionOrderForm")
    })
    @PutMapping(value = "/submitOrder/{id}")
    public Result submitOrder(@PathVariable Long id, @Valid @RequestBody InspectionOrderForm inspectionOrderForm) {
        InspectionOrder inspectionOrder = inspectionOrderForm.toPo(id, InspectionOrder.class);
        return Result.success(inspectionOrderService.submitOrder(inspectionOrder));
    }




    @ApiOperation(value = "??????????????????Excel", notes = "??????????????????Excel")
    @PostMapping(value = "/exportInspectionOrderExcel")
    public void exportInspectionOrderExcel(@Valid @RequestBody InspectionOrderQueryForm inspectionOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", inspectionOrderQueryForm);
        List<InspectionOrderAndItemVO> inspectionOrderAndItemVOList = inspectionOrderService.queryInspectionOrderByCondition(inspectionOrderQueryForm.toParam(InspectionOrderQueryParam.class));


        //?????????????????????
        //????????????
        Result orderStatusResult = dataDictProvider.getDataDictList(DataDictConstants.INSPECTION_ORDER_STATUS);
        HashMap<String, String> orderStatusMap = new HashMap<String, String>();
        if(orderStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)orderStatusResult.getData();
            orderStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("??????" + DataDictConstants.INSPECTION_ORDER_STATUS + "?????????????????????" + orderStatusResult.getMsg());
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
        titleRow.createCell(10).setCellValue("????????????");
        titleRow.createCell(11).setCellValue("??????????????????");
        titleRow.createCell(12).setCellValue("??????????????????");
        titleRow.createCell(13).setCellValue("?????????");
        titleRow.createCell(14).setCellValue("???????????????");
        titleRow.createCell(15).setCellValue("???????????????");
        titleRow.createCell(16).setCellValue("???????????????");
        titleRow.createCell(17).setCellValue("?????????????????????");
        titleRow.createCell(18).setCellValue("?????????");
        titleRow.createCell(19).setCellValue("?????????");
        titleRow.createCell(20).setCellValue("????????????");
        titleRow.createCell(21).setCellValue("????????????");
        titleRow.createCell(22).setCellValue("??????????????????");
        titleRow.createCell(23).setCellValue("??????????????????");
//        titleRow.createCell(24).setCellValue("??????????????????");
        titleRow.createCell(25).setCellValue("????????????");
        titleRow.createCell(26).setCellValue("?????????");
        titleRow.createCell(27).setCellValue("????????????");
        titleRow.createCell(28).setCellValue("?????????");
        titleRow.createCell(29).setCellValue("????????????");

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
                    //??????????????????????????????
                    String orderStatus = inspectionOrderAndItemVO.getStatus() != null ? inspectionOrderAndItemVO.getStatus() + "" : "";
                    if(StringUtils.isNotEmpty(orderStatus))
                    {
                        if(orderStatusMap.containsKey(orderStatus))
                        {
                            orderStatus = orderStatusMap.get(orderStatus);
                        }
                    }
                    dataRow.createCell(8).setCellValue(orderStatus);
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

                        //???????????????????????????????????????
                        String itemType = inspectionOrderItem.getItemType() != null ? inspectionOrderItem.getItemType() + "" : "";
                        if(StringUtils.isNotEmpty(itemType))
                        {
                            if(itemTypeMap.containsKey(itemType))
                            {
                                itemType = itemTypeMap.get(itemType);
                            }
                        }
                        dataRow.createCell(14).setCellValue(itemType);
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
                        dataRow.createCell(17).setCellValue(inspectionOrderItem.getCheckItemStandard() != null ? inspectionOrderItem.getCheckItemStandard() + "" : "");
                        dataRow.createCell(18).setCellValue(inspectionOrderItem.getTheoreticalValue() != null ? inspectionOrderItem.getTheoreticalValue() + "" : "");

                        if(inspectionOrderItem.getActualValue() != null) {
                            dataRow.createCell(19).setCellValue(Double.valueOf(inspectionOrderItem.getActualValue() + ""));
                        }else{
                            dataRow.createCell(19).setCellType(CellType.BLANK);
                        }


                        String isFinish = inspectionOrderItem.getIsFinish() != null ? inspectionOrderItem.getIsFinish() + "" : "";
                        if(StringUtils.isNotEmpty(isFinish))
                        {
                            if(yesNoMap.containsKey(isFinish))
                            {
                                isFinish = yesNoMap.get(isFinish);
                            }
                        }
                        dataRow.createCell(20).setCellValue(isFinish);
                        dataRow.createCell(21).setCellValue(inspectionOrderItem.getCheckResult() != null ? inspectionOrderItem.getCheckResult() + "" : "");

                        String isException = inspectionOrderItem.getIsException() != null ? inspectionOrderItem.getIsException() + "" : "";
                        if(StringUtils.isNotEmpty(isException))
                        {
                            if(yesNoMap.containsKey(isException))
                            {
                                isException = yesNoMap.get(isException);
                            }
                        }
                        dataRow.createCell(22).setCellValue(isException);

                        String isFault = inspectionOrderItem.getIsFault() != null ? inspectionOrderItem.getIsFault() + "" : "";
                        if(StringUtils.isNotEmpty(isFault))
                        {
                            if(yesNoMap.containsKey(isFault))
                            {
                                isFault = yesNoMap.get(isFault);
                            }
                        }
                        dataRow.createCell(23).setCellValue(isFault);

//                        String isRepair = inspectionOrderItem.getIsRepair() != null ? inspectionOrderItem.getIsRepair() + "" : "";
//                        if(StringUtils.isNotEmpty(isRepair))
//                        {
//                            if(yesNoMap.containsKey(isRepair))
//                            {
//                                isRepair = yesNoMap.get(isRepair);
//                            }
//                        }
//                        dataRow.createCell(24).setCellValue(isRepair);
                        dataRow.createCell(24).setCellValue(inspectionOrderItem.getFaultDesc() != null ? inspectionOrderItem.getFaultDesc() + "" : "");
                        dataRow.createCell(25).setCellValue(inspectionOrderItem.getUpdatedBy() != null ? inspectionOrderItem.getUpdatedBy() + "" : "");
                        dataRow.createCell(26).setCellValue(inspectionOrderItem.getUpdatedTime() != null ? inspectionOrderItem.getUpdatedTime().format(dateTimeFormatter) + "" : "");
                        dataRow.createCell(27).setCellValue(inspectionOrderItem.getCreatedBy() != null ? inspectionOrderItem.getCreatedBy() + "" : "");
                        dataRow.createCell(28).setCellValue(inspectionOrderItem.getCreatedTime() != null ? inspectionOrderItem.getCreatedTime().format(dateTimeFormatter) + "" : "");
                    }

                    //?????????????????????
                    for(int k=1; k<=12; k++) {
                        if(inspectionOrderItemList.size() <= 1)
                        {
                            continue;
                        }
                        ExcelUtil.mergeRegion(wbSheet, rowNumber-inspectionOrderItemList.size(), rowNumber-1, k, k);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*20, 256*15, 256*20, 256*15, 256*15, 256*10, 256*15,
                                                              256*10, 256*20, 256*20, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15,
                                                              256*15, 256*15, 256*15, 256*15, 256*15, 256*20, 256*15, 256*20});

        } catch (Exception exception)
        {
            log.error("????????????????????????", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "????????????.xlsx");
    }

}