package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.param.MoldingAnalysisDataParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingDataParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingDataPageParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingQueryDataParam;
import com.aacoptics.wlg.dashboard.service.MoldingEventDataService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.aacoptics.wlg.dashboard.util.DateUtil;
import com.aacoptics.wlg.dashboard.util.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moldingMachineParam")
@Api("moldingMachineParam")
@Slf4j
public class MoldingParamController {

    @Resource()
    MoldingMachineParamDataService moldingMachineParamDataService;

    @Resource()
    MoldingEventDataService moldingEventDataService;


    @ApiOperation(value = "获取所有机台号", notes = "获取所有机台号")
    @GetMapping("/getMoldingMachineName")
    public Result getMoldingMachineName() {
        return Result.success(moldingMachineParamDataService.getMachineName());
    }


    @ApiOperation(value = "获取这段时间的waferId", notes = "获取这段时间的waferId")
    @GetMapping(value = "/getWaferIds")
    public Result getWaferIds(@RequestParam String machineName,
                              @RequestParam String startTime,
                              @RequestParam String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingMachineParamDataService.getWaferIds(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df)));
    }

    @ApiOperation(value = "获取这段时间的报警事件", notes = "获取这段时间的报警事件")
    @GetMapping(value = "/getMachineEvents")
    public Result getMachineEvents(@RequestParam String machineName,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingEventDataService.getMachineEvents(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df), new Page(current, size)));
    }

    @ApiOperation(value = "获取这段时间的错误事件", notes = "获取这段时间的错误事件")
    @GetMapping(value = "/getMachineErrors")
    public Result getMachineErrors(@RequestParam String machineName,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingEventDataService.getMachineErrors(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df), new Page(current, size)));
    }

//    @ApiOperation(value = "获取这段时间的机台状态", notes = "获取这段时间的机台状态")
//    @GetMapping(value = "/getMachineStatus")
//    public Result getMachineStatus(@RequestParam List<String> machineName,
//                                   @RequestParam String startTime,
//                                   @RequestParam String endTime,
//                                   @RequestParam Long current,
//                                   @RequestParam Long size) {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return Result.success(moldingEventDataService.getMachineStatus(machineName,
//                LocalDateTime.parse(startTime, df),
//                LocalDateTime.parse(endTime, df), new Page(current, size)));
//    }

    @ApiOperation(value = "获取这段时间的异常数据", notes = "获取这段时间的异常数据")
    @GetMapping(value = "/getMachineAbnormalData")
    public Result getMachineAbnormalData(@RequestParam String machineName,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam Long current,
                                   @RequestParam Long size) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Result.success(moldingEventDataService.getMachineAbnormalData(machineName,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df), new Page(current, size)));
    }

    @ApiOperation(value = "获取模造机参数", notes = "获取模造机参数")
    @PostMapping(value = "/getMoldParam")
    public Result getParamData(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamData(moldingDataParam.getMachineName(), moldingDataParam.getParamName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机参数", notes = "获取模造机参数")
    @PostMapping(value = "/getMoldParamArray")
    public Result getMoldParamArray(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamDataArray(moldingDataParam.getMachineName(), moldingDataParam.getParamName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机参数名", notes = "获取模造机参数名")
    @PostMapping(value = "/getMoldParamName")
    public Result getMoldParamName(@RequestBody MoldingDataParam moldingDataParam) {
        return Result.success(moldingMachineParamDataService.getMoldingParamName(moldingDataParam.getMachineName(), moldingDataParam.getWaferIds()));
    }

    @ApiOperation(value = "获取模造机统计数据", notes = "获取模造机统计数据")
    @PostMapping(value = "/getAnalysisData")
    public Result getAnalysisData(@RequestBody MoldingAnalysisDataParam moldingAnalysisDataParam) {
        return Result.success(moldingMachineParamDataService.getAnalysisData(moldingAnalysisDataParam.getMachineName(),
                moldingAnalysisDataParam.getParamNames(),
                moldingAnalysisDataParam.getStartTime(),
                moldingAnalysisDataParam.getEndTime()));
    }

    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@RequestBody MoldingQueryDataParam moldingQueryDataParam, HttpServletResponse response) throws Exception{
        List<Map<String, Object>> data = moldingMachineParamDataService.getMoldingStatusData(moldingQueryDataParam.getMachineName(), moldingQueryDataParam.getStartTime(), moldingQueryDataParam.getEndTime());
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("模造机状态报表");
        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("机台号");
        titleRow.createCell(1).setCellValue("状态");
        titleRow.createCell(2).setCellValue("持续时间");
        try {
            if(data != null && data.size() > 0) {
                for(int i = 0; i < data.size(); i++) {
                    Map<String, Object> dataMap = data.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(dataMap.get("machine_name") + "");
                    dataRow.createCell(1).setCellValue(dataMap.get("alarm_info") + "");
                    dataRow.createCell(2).setCellValue(DateUtil.formatSeconds(Integer.parseInt(String.valueOf(dataMap.get("duration")))));
                }
            }
            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15});
        } catch(Exception e) {
            throw e;
        }
        ExcelUtil.exportXlsx(response, workbook, "模造机状态报表.xlsx");
    }

    @ApiOperation(value = "获取这段时间的机台状态", notes = "获取这段时间的机台状态")
    @PostMapping(value = "/getMachineStatus")
    public Result getMachineStatus(@RequestBody MoldingDataPageParam moldingDataPageParam) {
        List<String> machineNameList = moldingDataPageParam.getMachineName();
        if(machineNameList == null || machineNameList.size() == 0) {
            machineNameList.add("");
        }
        return Result.success(moldingEventDataService.getMachineStatus(machineNameList,
                moldingDataPageParam.getStartTime(),
                moldingDataPageParam.getEndTime(), new Page(moldingDataPageParam.getCurrent(), moldingDataPageParam.getSize())));
    }

    @ApiOperation(value = "获取MK4Logfile参数", notes = "获取MK4Logfile参数")
    @PostMapping(value = "/getMoldingMK4Data")
    public Result getMoldingMK4Data(@RequestBody MoldingDataPageParam moldingDataPageParam) {
        List<String> machineNameList = moldingDataPageParam.getMachineName();
        if(machineNameList == null || machineNameList.size() == 0) {
            machineNameList.add("");
        }
        return Result.success(moldingEventDataService.getMoldingMK4Data(machineNameList,
                moldingDataPageParam.getStartTime(),
                moldingDataPageParam.getEndTime(), new Page(moldingDataPageParam.getCurrent(), moldingDataPageParam.getSize())));
    }

    @ApiOperation(value = "导出MK4Logfile参数excel", notes = "导出MK4Logfile参数excel")
    @PostMapping(value = "/exportMoldingMK4DataExcel")
    public void exportMoldingMK4DataExcel(@RequestBody MoldingQueryDataParam moldingQueryDataParam, HttpServletResponse response) throws Exception{
        List<Map<String, Object>> data = moldingMachineParamDataService.getMoldingMk4ExportData(moldingQueryDataParam.getMachineName(),
                moldingQueryDataParam.getStartTime(), moldingQueryDataParam.getEndTime());
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("MK4Logfile参数报表");
        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("机台号");
        titleRow.createCell(1).setCellValue("批次号");
        titleRow.createCell(2).setCellValue("配方");
        titleRow.createCell(3).setCellValue("wafer类型");
        titleRow.createCell(4).setCellValue("开始时间");
        titleRow.createCell(5).setCellValue("Ph1_UpHeatingRateSetpoint");
        titleRow.createCell(6).setCellValue("Ph1_LowHeatingRateSetpoint");
        titleRow.createCell(7).setCellValue("Ph1_Position");
        titleRow.createCell(8).setCellValue("Ph1_Pressure");
        titleRow.createCell(9).setCellValue("Ph2_TempUpSetpoint");
        titleRow.createCell(10).setCellValue("Ph2_TempLowSetpoint");
        titleRow.createCell(11).setCellValue("Ph2_Position");
        titleRow.createCell(12).setCellValue("Ph2_ForceMax");
        titleRow.createCell(13).setCellValue("Ph2_ForceMin");
        titleRow.createCell(14).setCellValue("Ph3_TempUpSetpoint");
        titleRow.createCell(15).setCellValue("Ph3_TempLowSetpoint");
        titleRow.createCell(16).setCellValue("Ph3_Pressure");
        titleRow.createCell(17).setCellValue("Ph3_PumpTime");
        titleRow.createCell(18).setCellValue("Ph3_TempUpActual");
        titleRow.createCell(19).setCellValue("Ph3_TempLowActual");
        titleRow.createCell(20).setCellValue("Ph4_Pressure");
        titleRow.createCell(21).setCellValue("Ph4_TempUpSetpoint");
        titleRow.createCell(22).setCellValue("Ph4_TempLowSetpoint");
        titleRow.createCell(23).setCellValue("Ph4_TempUpActualMax");
        titleRow.createCell(24).setCellValue("Ph4_TempUpActualMin");
        titleRow.createCell(25).setCellValue("Ph4_TempLowActualMax");
        titleRow.createCell(26).setCellValue("Ph4_TempLowActualMin");
        titleRow.createCell(27).setCellValue("Ph4_SoakingTime");
        titleRow.createCell(28).setCellValue("Ph5_Position");
        titleRow.createCell(29).setCellValue("Ph5_ForceMax");
        titleRow.createCell(30).setCellValue("Ph5_ForceMin");
        titleRow.createCell(31).setCellValue("Ph6_TempUpSetpoint");
        titleRow.createCell(32).setCellValue("Ph6_TempLowSetpoint");
        titleRow.createCell(33).setCellValue("Ph6_FRaisingRate");
        titleRow.createCell(34).setCellValue("Ph7_TempUpSetpoint");
        titleRow.createCell(35).setCellValue("Ph7_TempLowSetpoint");
        titleRow.createCell(36).setCellValue("Ph7_TempUpActualMax");
        titleRow.createCell(37).setCellValue("Ph7_TempUpActualMin");
        titleRow.createCell(38).setCellValue("Ph7_TempLowActualMax");
        titleRow.createCell(39).setCellValue("Ph7_TempLowActualMin");
        titleRow.createCell(40).setCellValue("Ph7_Force");
        titleRow.createCell(41).setCellValue("Ph7_Position");
        titleRow.createCell(42).setCellValue("Ph7_Pressure");
        titleRow.createCell(43).setCellValue("Ph7_MoldingTime");
        titleRow.createCell(44).setCellValue("Ph8_UpCoolingRateSetpoint");
        titleRow.createCell(45).setCellValue("Ph8_LowCoolingRateSetpoint");
        titleRow.createCell(46).setCellValue("Ph8_Force");
        titleRow.createCell(47).setCellValue("Ph8_Pressure");
        titleRow.createCell(48).setCellValue("Ph8_TempUpActual");
        titleRow.createCell(49).setCellValue("Ph8_TempLowActual");
        titleRow.createCell(50).setCellValue("Ph8_TempUpLow");
        titleRow.createCell(51).setCellValue("Ph9_UpCoolingRateSetpoint");
        titleRow.createCell(52).setCellValue("Ph9_LowCoolingRateSetpoint");
        titleRow.createCell(53).setCellValue("Ph9_TempUpActual");
        titleRow.createCell(54).setCellValue("Ph9_TempLowActual");
        titleRow.createCell(55).setCellValue("Ph9_TempUpLow");
        titleRow.createCell(56).setCellValue("Ph10_TempUpActual_30N");
        titleRow.createCell(57).setCellValue("Ph10_TempLowActual_30N");
        titleRow.createCell(58).setCellValue("Ph10_TempUpLow_30N");
        titleRow.createCell(59).setCellValue("Ph10_ForceNegative");
        titleRow.createCell(60).setCellValue("Ph10_TimeNegative");
        titleRow.createCell(61).setCellValue("Ph10_TempUpActual_Negative");
        titleRow.createCell(62).setCellValue("Ph10_TempLowActual_Negative");
        titleRow.createCell(63).setCellValue("Ph10_TempUpLow_Negative");
        titleRow.createCell(64).setCellValue("Ph11_Position");
        titleRow.createCell(65).setCellValue("Ph12_UpCoolingRateSetpoint");
        titleRow.createCell(66).setCellValue("Ph12_LowCoolingRateSetpoint");
        titleRow.createCell(67).setCellValue("Ph12_TempLowActual");
        titleRow.createCell(68).setCellValue("Ph12_PickPlaceTempActual");
        titleRow.createCell(69).setCellValue("Ph12_ExchangeTempActual");
        titleRow.createCell(70).setCellValue("TempUpActual_Fp1");
        titleRow.createCell(71).setCellValue("TempLowActual_Fp1");
        titleRow.createCell(72).setCellValue("TempUpLow_Fp1");
        titleRow.createCell(73).setCellValue("TempUpheatbedAverage_Fp1");
        titleRow.createCell(74).setCellValue("TempLowheatbedAverage_Fp1");
        titleRow.createCell(75).setCellValue("TempUpActual_Fp2");
        titleRow.createCell(76).setCellValue("TempLowActual_Fp2");
        titleRow.createCell(77).setCellValue("TempUpLow_Fp2");
        titleRow.createCell(78).setCellValue("TempUpheatbedAverage_Fp2");
        titleRow.createCell(79).setCellValue("TempLowheatbedAverage_Fp2");
        titleRow.createCell(80).setCellValue("TempUpActual_Fp3");
        titleRow.createCell(81).setCellValue("TempLowActual_Fp3");
        titleRow.createCell(82).setCellValue("TempUpLow_Fp3");
        titleRow.createCell(83).setCellValue("TempUpheatbedAverage_Fp3");
        titleRow.createCell(84).setCellValue("TempLowheatbedAverage_Fp3");
        try{
            if(data != null && data.size() > 0) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                for(int i = 0; i < data.size(); i++) {
                    Map<String, Object> dataMap = data.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(dataMap.get("equipmentName") + "");
                    dataRow.createCell(1).setCellValue(dataMap.get("batchId") + "");
                    dataRow.createCell(2).setCellValue(dataMap.get("recipeName") + "");
                    dataRow.createCell(3).setCellValue(dataMap.get("waferType") + "");
                    dataRow.createCell(4).setCellValue(((Timestamp) dataMap.get("logFileStartTime")).toLocalDateTime().format(dateTimeFormatter));
                    dataRow.createCell(5).setCellValue(Double.valueOf(dataMap.get("ph1UpHeatingRateSetPoint") + ""));
                    dataRow.createCell(6).setCellValue(Double.valueOf(dataMap.get("ph1LowHeatingRateSetPoint") + ""));
                    dataRow.createCell(7).setCellValue(Double.valueOf(dataMap.get("ph1Position") + ""));
                    dataRow.createCell(8).setCellValue(Double.valueOf(dataMap.get("ph1Pressure") + ""));
                    dataRow.createCell(9).setCellValue(Double.valueOf(dataMap.get("ph2TempUpSetPoint") + ""));
                    dataRow.createCell(10).setCellValue(Double.valueOf(dataMap.get("ph2TempLowSetPoint") + ""));
                    dataRow.createCell(11).setCellValue(Double.valueOf(dataMap.get("ph2Position") + ""));
                    dataRow.createCell(12).setCellValue(Double.valueOf(dataMap.get("ph2ForceMax") + ""));
                    dataRow.createCell(13).setCellValue(Double.valueOf(dataMap.get("ph2ForceMin") + ""));
                    dataRow.createCell(14).setCellValue(Double.valueOf(dataMap.get("ph3TempUpSetPoint") + ""));
                    dataRow.createCell(15).setCellValue(Double.valueOf(dataMap.get("ph3TempLowSetPoint") + ""));
                    dataRow.createCell(16).setCellValue(Double.valueOf(dataMap.get("ph3Pressure") + ""));
                    dataRow.createCell(17).setCellValue(Double.valueOf(dataMap.get("ph3PumpTime") + ""));
                    dataRow.createCell(18).setCellValue(Double.valueOf(dataMap.get("ph3TempUpActual") + ""));
                    dataRow.createCell(19).setCellValue(Double.valueOf(dataMap.get("ph3TempLowActual") + ""));
                    dataRow.createCell(20).setCellValue(Double.valueOf(dataMap.get("ph4Pressure") + ""));
                    dataRow.createCell(21).setCellValue(Double.valueOf(dataMap.get("ph4TempUpSetPoint") + ""));
                    dataRow.createCell(22).setCellValue(Double.valueOf(dataMap.get("ph4TempLowSetPoint") + ""));
                    dataRow.createCell(23).setCellValue(Double.valueOf(dataMap.get("ph4TempUpActualMax") + ""));
                    dataRow.createCell(24).setCellValue(Double.valueOf(dataMap.get("ph4TempUpActualMin") + ""));
                    dataRow.createCell(25).setCellValue(Double.valueOf(dataMap.get("ph4TempLowActualMax") + ""));
                    dataRow.createCell(26).setCellValue(Double.valueOf(dataMap.get("ph4TempLowActualMin") + ""));
                    dataRow.createCell(27).setCellValue(Double.valueOf(dataMap.get("ph4SoakingTime") + ""));
                    dataRow.createCell(28).setCellValue(Double.valueOf(dataMap.get("ph5Position") + ""));
                    dataRow.createCell(29).setCellValue(Double.valueOf(dataMap.get("ph5ForceMax") + ""));
                    dataRow.createCell(30).setCellValue(Double.valueOf(dataMap.get("ph5ForceMin") + ""));
                    dataRow.createCell(31).setCellValue(Double.valueOf(dataMap.get("ph6TempUpSetPoint") + ""));
                    dataRow.createCell(32).setCellValue(Double.valueOf(dataMap.get("ph6TempLowSetPoint") + ""));
                    dataRow.createCell(33).setCellValue(Double.valueOf(dataMap.get("ph6FRaisingRate") + ""));
                    dataRow.createCell(34).setCellValue(Double.valueOf(dataMap.get("ph7TempUpSetPoint") + ""));
                    dataRow.createCell(35).setCellValue(Double.valueOf(dataMap.get("ph7TempLowSetPoint") + ""));
                    dataRow.createCell(36).setCellValue(Double.valueOf(dataMap.get("ph7TempUpActualMax") + ""));
                    dataRow.createCell(37).setCellValue(Double.valueOf(dataMap.get("ph7TempUpActualMin") + ""));
                    dataRow.createCell(38).setCellValue(Double.valueOf(dataMap.get("ph7TempLowActualMax") + ""));
                    dataRow.createCell(39).setCellValue(Double.valueOf(dataMap.get("ph7TempLowActualMin") + ""));
                    dataRow.createCell(40).setCellValue(Double.valueOf(dataMap.get("ph7Force") + ""));
                    dataRow.createCell(41).setCellValue(Double.valueOf(dataMap.get("ph7Position") + ""));
                    dataRow.createCell(42).setCellValue(Double.valueOf(dataMap.get("ph7Pressure") + ""));
                    dataRow.createCell(43).setCellValue(Double.valueOf(dataMap.get("ph7MoldingTime") + ""));
                    dataRow.createCell(44).setCellValue(Double.valueOf(dataMap.get("ph8UpCoolingRateSetPoint") + ""));
                    dataRow.createCell(45).setCellValue(Double.valueOf(dataMap.get("ph8LowCoolingRateSetPoint") + ""));
                    dataRow.createCell(46).setCellValue(Double.valueOf(dataMap.get("ph8Force") + ""));
                    dataRow.createCell(47).setCellValue(Double.valueOf(dataMap.get("ph8Pressure") + ""));
                    dataRow.createCell(48).setCellValue(Double.valueOf(dataMap.get("ph8TempUpActual") + ""));
                    dataRow.createCell(49).setCellValue(Double.valueOf(dataMap.get("ph8TempLowActual") + ""));
                    dataRow.createCell(50).setCellValue(Double.valueOf(dataMap.get("ph8TempUpLow") + ""));
                    dataRow.createCell(51).setCellValue(Double.valueOf(dataMap.get("ph9UpCoolingRateSetPoint") + ""));
                    dataRow.createCell(52).setCellValue(Double.valueOf(dataMap.get("ph9LowCoolingRateSetPoint") + ""));
                    dataRow.createCell(53).setCellValue(Double.valueOf(dataMap.get("ph9TempUpActual") + ""));
                    dataRow.createCell(54).setCellValue(Double.valueOf(dataMap.get("ph9TempLowActual") + ""));
                    dataRow.createCell(55).setCellValue(Double.valueOf(dataMap.get("ph9TempUpLow") + ""));
                    dataRow.createCell(56).setCellValue(Double.valueOf(dataMap.get("ph10TempUpActual30N") + ""));
                    dataRow.createCell(57).setCellValue(Double.valueOf(dataMap.get("ph10TempLowActual30N") + ""));
                    dataRow.createCell(58).setCellValue(Double.valueOf(dataMap.get("ph10TempUpLow30N") + ""));
                    dataRow.createCell(59).setCellValue(Double.valueOf(dataMap.get("ph10ForceNegative") + ""));
                    dataRow.createCell(60).setCellValue(Double.valueOf(dataMap.get("ph10TimeNegative") + ""));
                    dataRow.createCell(61).setCellValue(Double.valueOf(dataMap.get("ph10TempUpActualNegative") + ""));
                    dataRow.createCell(62).setCellValue(Double.valueOf(dataMap.get("ph10TempLowActualNegative") + ""));
                    dataRow.createCell(63).setCellValue(Double.valueOf(dataMap.get("ph10TempUpLowNegative") + ""));
                    dataRow.createCell(64).setCellValue(Double.valueOf(dataMap.get("ph11Position") + ""));
                    dataRow.createCell(65).setCellValue(Double.valueOf(dataMap.get("p12UpCoolingRateSetPoint") + ""));
                    dataRow.createCell(66).setCellValue(Double.valueOf(dataMap.get("p12LowCoolingRateSetPoint") + ""));
                    dataRow.createCell(67).setCellValue(Double.valueOf(dataMap.get("p12TempLowActual") + ""));
                    dataRow.createCell(68).setCellValue(Double.valueOf(dataMap.get("ph12PickPlaceTempActual") + ""));
                    dataRow.createCell(69).setCellValue(Double.valueOf(dataMap.get("ph12ExchangeTempActual") + ""));
                    dataRow.createCell(70).setCellValue(Double.valueOf(dataMap.get("tempUpActualFp1") + ""));
                    dataRow.createCell(71).setCellValue(Double.valueOf(dataMap.get("tempLowActualFp1") + ""));
                    dataRow.createCell(72).setCellValue(Double.valueOf(dataMap.get("tempUpLowFp1") + ""));
                    dataRow.createCell(73).setCellValue(Double.valueOf(dataMap.get("tempUpHeatBedAverageFp1") + ""));
                    dataRow.createCell(74).setCellValue(Double.valueOf(dataMap.get("tempLowHeatBedAverageFp1") + ""));
                    dataRow.createCell(75).setCellValue(Double.valueOf(dataMap.get("tempUpActualFp2") + ""));
                    dataRow.createCell(76).setCellValue(Double.valueOf(dataMap.get("tempLowActualFp2") + ""));
                    dataRow.createCell(77).setCellValue(Double.valueOf(dataMap.get("tempUpLowFp2") + ""));
                    dataRow.createCell(78).setCellValue(Double.valueOf(dataMap.get("tempUpHeatBedAverageFp2") + ""));
                    dataRow.createCell(79).setCellValue(Double.valueOf(dataMap.get("tempLowHeatBedAverageFp2") + ""));
                    dataRow.createCell(80).setCellValue(Double.valueOf(dataMap.get("tempUpActualFp3") + ""));
                    dataRow.createCell(81).setCellValue(Double.valueOf(dataMap.get("tempLowActualFp3") + ""));
                    dataRow.createCell(82).setCellValue(Double.valueOf(dataMap.get("tempUpLowFp3") + ""));
                    dataRow.createCell(83).setCellValue(Double.valueOf(dataMap.get("tempUpHeatBedAverageFp3") + ""));
                    dataRow.createCell(84).setCellValue(Double.valueOf(dataMap.get("tempLowHeatBedAverageFp3") + ""));

                }
            }
        } catch (Exception e) {
            throw e;
        }
        ExcelUtil.exportXlsx(response, workbook, "MK4Logfile参数报表.xlsx");

    }
}