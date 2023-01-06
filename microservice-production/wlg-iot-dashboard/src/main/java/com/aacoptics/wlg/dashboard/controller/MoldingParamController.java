package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.param.MoldingAnalysisDataParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingDataParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingStatusDataPageParam;
import com.aacoptics.wlg.dashboard.entity.param.MoldingStatusDataParam;
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
    public void exportExcel(@RequestBody MoldingStatusDataParam moldingStatusDataParam, HttpServletResponse response) throws Exception{
        List<Map<String, Object>> data = moldingMachineParamDataService.getMoldingStatusData(moldingStatusDataParam.getMachineName(), moldingStatusDataParam.getStartTime(), moldingStatusDataParam.getEndTime());
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
    public Result getMachineStatus(@RequestBody MoldingStatusDataPageParam moldingStatusDataPageParam) {
        return Result.success(moldingEventDataService.getMachineStatus(moldingStatusDataPageParam.getMachineName(),
                moldingStatusDataPageParam.getStartTime(),
                moldingStatusDataPageParam.getEndTime(), new Page(moldingStatusDataPageParam.getCurrent(), moldingStatusDataPageParam.getSize())));
    }
}