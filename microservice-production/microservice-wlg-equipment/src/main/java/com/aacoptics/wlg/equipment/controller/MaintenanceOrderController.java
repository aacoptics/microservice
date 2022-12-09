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

    @ApiOperation(value = "搜索保养工单", notes = "根据条件搜索保养工单信息")
    @ApiImplicitParam(name = "projectMapQueryForm", value = "保养工单查询参数", required = true, dataType = "ProjectMapQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryDetail")
    public Result queryDetail(@Valid @RequestBody MaintenanceOrderQueryForm maintenanceOrderQueryForm) {
        log.debug("query with name:{}", maintenanceOrderQueryForm);
        return Result.success(maintenanceOrderService.queryDetail(maintenanceOrderQueryForm.getPage(), maintenanceOrderQueryForm.toParam(MaintenanceOrderQueryParam.class)));
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

        MaintenanceOrder maintenanceOrderTarget = maintenanceOrderService.get(id);
        if(MaintenanceOrderStatusConstants.COMMITTED.equals(maintenanceOrderTarget.getStatus()))
        {
            throw new BusinessException("工单已保养，不能更新接单人");
        }
        if(MaintenanceOrderStatusConstants.CONFIRMED.equals(maintenanceOrderTarget.getStatus()))
        {
            throw new BusinessException("工单已确认，不能更新接单人");
        }


        maintenanceOrderTarget.setDutyPersonId(maintenanceOrderForm.getDutyPersonId() != null ? maintenanceOrderForm.getDutyPersonId().trim() : null);

        return Result.success(maintenanceOrderService.update(maintenanceOrderTarget));
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

    @ApiOperation(value = "根据设用户查询保养工单", notes = "根据设用户查询保养工单")
    @ApiImplicitParam(name = "requestBody", value = "用户", required = true, dataType = "String")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
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

        //获取数据字典值
        //工单状态
        Result orderStatusResult = dataDictProvider.getDataDictList(DataDictConstants.MAINTENANCE_ORDER_STATUS);
        HashMap<String, String> orderStatusMap = new HashMap<String, String>();
        if(orderStatusResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)orderStatusResult.getData();
            orderStatusMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.MAINTENANCE_ORDER_STATUS + "数据字典失败，" + orderStatusResult.getMsg());
        }
        //是否
        Result yesNoResult = dataDictProvider.getDataDictList(DataDictConstants.YES_NO);
        HashMap<String, String> yesNoMap = new HashMap<String, String>();
        if(yesNoResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)yesNoResult.getData();
            yesNoMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.YES_NO + "数据字典失败，" + yesNoResult.getMsg());
        }
        //保养项类型
        Result itemTypeResult = dataDictProvider.getDataDictList(DataDictConstants.ITEM_TYPE);
        HashMap<String, String> itemTypeMap = new HashMap<String, String>();
        if(itemTypeResult.isSuccess())
        {
            List<HashMap<String, Object>> dataDictList =  (List<HashMap<String, Object>>)itemTypeResult.getData();
            itemTypeMap = DataDictUtil.convertDataDictListToMap(dataDictList);
        }
        else {
            log.error("获取" + DataDictConstants.ITEM_TYPE + "数据字典失败，" + itemTypeResult.getMsg());
        }

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
        titleRow.createCell(11).setCellValue("保养项类型");
        titleRow.createCell(12).setCellValue("起始范围值");
        titleRow.createCell(13).setCellValue("截至范围值");
        titleRow.createCell(14).setCellValue("保养项判断标准");
        titleRow.createCell(15).setCellValue("理论值");
        titleRow.createCell(16).setCellValue("实际值");
        titleRow.createCell(17).setCellValue("是否完成");
        titleRow.createCell(18).setCellValue("保养结果");
        titleRow.createCell(19).setCellValue("是否存在异常");
        titleRow.createCell(20).setCellValue("是否存在故障");
//        titleRow.createCell(21).setCellValue("是否需要维修");
        titleRow.createCell(22).setCellValue("故障描述");
        titleRow.createCell(23).setCellValue("更新人");
        titleRow.createCell(24).setCellValue("更新时间");
        titleRow.createCell(25).setCellValue("创建人");
        titleRow.createCell(26).setCellValue("创建时间");

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

                    //状态通过数据字典翻译
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

                        //保养项类型通过数据字典翻译
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

                    //合并主表单元格
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
            log.error("导出保养工单异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "保养工单.xlsx");
    }

}