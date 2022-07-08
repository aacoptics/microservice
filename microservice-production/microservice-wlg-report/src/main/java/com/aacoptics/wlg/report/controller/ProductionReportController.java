package com.aacoptics.wlg.report.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.report.entity.form.ProductionDayReportQueryForm;
import com.aacoptics.wlg.report.entity.form.ProductionMonthReportQueryForm;
import com.aacoptics.wlg.report.entity.form.ProductionProjectReportQueryForm;
import com.aacoptics.wlg.report.entity.param.ProductionDayReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionMonthReportQueryParam;
import com.aacoptics.wlg.report.entity.param.ProductionProjectReportQueryParam;
import com.aacoptics.wlg.report.service.ProductionReportService;
import com.aacoptics.wlg.report.util.ExcelUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productionReport")
@Api("productionReport")
@Slf4j
public class ProductionReportController {

    @Autowired
    ProductionReportService productionReportService;


    @ApiOperation(value = "搜索生产月度汇总列转列表头", notes = "搜索生产月度汇总列转列表头")
    @ApiImplicitParam(name = "productionMonthReportQueryForm", value = "生产月度汇总查询参数", required = true, dataType = "ProductionMonthReportQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionMonthReportTitleByCondition")
    public Result queryProductionMonthReportTitleByCondition(@Valid @RequestBody ProductionMonthReportQueryForm productionMonthReportQueryForm) {
        log.debug("queryProductionMonthReportTitleByCondition with name:{}", productionMonthReportQueryForm);
        return Result.success(productionReportService.queryProductionMonthReportTitleByCondition(productionMonthReportQueryForm.toParam(ProductionMonthReportQueryParam.class)));
    }


    @ApiOperation(value = "搜索生产月度汇总", notes = "根据条件搜索生产月度汇总信息")
    @ApiImplicitParam(name = "productionMonthReportQueryForm", value = "生产月度汇总查询参数", required = true, dataType = "ProductionMonthReportQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionMonthReportByCondition")
    public Result queryProductionMonthReportByCondition(@Valid @RequestBody ProductionMonthReportQueryForm productionMonthReportQueryForm) {
        log.debug("queryProductionMonthReportByCondition with name:{}", productionMonthReportQueryForm);
        return Result.success(productionReportService.queryProductionMonthReportByCondition(productionMonthReportQueryForm.toParam(ProductionMonthReportQueryParam.class)));
    }

    @ApiOperation(value = "搜索生产日报表", notes = "根据条件搜索搜索生产日报表")
    @ApiImplicitParam(name = "productionDayReportQueryForm", value = "搜索生产日报表查询参数", required = true, dataType = "ProductionDayReportQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionDayReportByCondition")
    public Result queryProductionDayReportByCondition(@Valid @RequestBody ProductionDayReportQueryForm productionDayReportQueryForm) {
        log.debug("queryProductionDayReportByCondition with name:{}", productionDayReportQueryForm);
        return Result.success(productionReportService.queryProductionDayReportByCondition(productionDayReportQueryForm.toParam(ProductionDayReportQueryParam.class)));
    }

    @ApiOperation(value = "搜索单个项目生产报表表头", notes = "搜索单个项目生产报表表头")
    @ApiImplicitParam(name = "productionMonthReportQueryForm", value = "单个项目生产报表表头查询参数", required = true, dataType = "ProductionMonthReportQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionProjectReportTitleByCondition")
    public Result queryProductionProjectReportTitleByCondition(@Valid @RequestBody ProductionProjectReportQueryForm productionProjectReportQueryForm) {
        log.debug("queryProductionProjectReportTitleByCondition with name:{}", productionProjectReportQueryForm);
        return Result.success(productionReportService.queryProductionProjectReportTitleByCondition(productionProjectReportQueryForm.toParam(ProductionProjectReportQueryParam.class)));
    }


    @ApiOperation(value = "搜索单个项目生产报表", notes = "根据条件搜索单个项目生产报表")
    @ApiImplicitParam(name = "productionProjectReportQueryForm", value = "搜索单个项目生产报表查询参数", required = true, dataType = "ProductionProjectReportQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/queryProductionProjectReportByCondition")
    public Result queryProductionProjectReportByCondition(@Valid @RequestBody ProductionProjectReportQueryForm productionProjectReportQueryForm) {
        log.debug("queryProductionProjectReportByCondition with name:{}", productionProjectReportQueryForm);
        return Result.success(productionReportService.queryProductionProjectReportByCondition(productionProjectReportQueryForm.toParam(ProductionProjectReportQueryParam.class)));
    }



    @ApiOperation(value = "导出每日产出报表Excel", notes = "导出每日产出报表Excel")
    @PostMapping(value = "/exportProductionDayExcel")
    public void exportProductionDayExcel(@Valid @RequestBody ProductionDayReportQueryForm productionDayReportQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", productionDayReportQueryForm);
        List<Map<String, Object>> productionDayList = productionReportService.queryProductionDayReportByCondition(productionDayReportQueryForm.toParam(ProductionDayReportQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("每日产出报表");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("日期");
        titleRow.createCell(2).setCellValue("项目");
        titleRow.createCell(3).setCellValue("模具");
        titleRow.createCell(4).setCellValue("周期");
        titleRow.createCell(5).setCellValue("计划收料穴");
        titleRow.createCell(6).setCellValue("实际收料穴");
        titleRow.createCell(7).setCellValue("计划产出");
        titleRow.createCell(8).setCellValue("预估直通率");
        titleRow.createCell(9).setCellValue("预估模压产出");
        titleRow.createCell(10).setCellValue("实际入库");

        try {
            if (productionDayList != null && productionDayList.size() > 0) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                for (int i = 0; i < productionDayList.size(); i++) {
                    Map<String, Object> productionDayMap = productionDayList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(i + 1);
                    dataRow.createCell(1).setCellValue(((Timestamp) productionDayMap.get("productionDate")).toLocalDateTime().format(dateTimeFormatter));
                    dataRow.createCell(2).setCellValue(productionDayMap.get("projectName") != null ? productionDayMap.get("projectName") + "" : "");
                    dataRow.createCell(3).setCellValue(productionDayMap.get("mold") != null ? productionDayMap.get("mold") + "" : "");
                    dataRow.createCell(4).setCellValue(productionDayMap.get("cycle") != null ? productionDayMap.get("cycle") + "" : "");

                    if(productionDayMap.containsKey("JHXUESHU") && productionDayMap.get("JHXUESHU") != null) {
                        dataRow.createCell(5).setCellValue(Double.valueOf(productionDayMap.get("JHXUESHU") + ""));
                    }else{
                        dataRow.createCell(5).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                    if(productionDayMap.containsKey("estimateHoleQty") && productionDayMap.get("estimateHoleQty") != null)
                    {
                        dataRow.createCell(6).setCellValue(Double.valueOf(productionDayMap.get("estimateHoleQty") + ""));
                    }else{
                        dataRow.createCell(6).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                    if(productionDayMap.containsKey("JHHDCHANCHU") && productionDayMap.get("JHHDCHANCHU") != null)
                    {
                        dataRow.createCell(7).setCellValue(Double.valueOf(productionDayMap.get("JHHDCHANCHU") + ""));
                    }else{
                        dataRow.createCell(7).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                    if(productionDayMap.containsKey("fpy") && productionDayMap.get("fpy") != null)
                    {
                        dataRow.createCell(8).setCellValue(productionDayMap.get("fpy") + "");
                    }else{
                        dataRow.createCell(8).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                    if(productionDayMap.containsKey("estimateHoleQty") && productionDayMap.get("estimateHoleQty") != null)
                    {
                        dataRow.createCell(9).setCellValue(Double.valueOf(productionDayMap.get("estimateHoleQty") + ""));
                    }else{
                        dataRow.createCell(9).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                    if(productionDayMap.containsKey("afterOutputQty") && productionDayMap.get("afterOutputQty") != null)
                    {
                        dataRow.createCell(10).setCellValue(Double.valueOf(productionDayMap.get("afterOutputQty") + ""));
                    }else{
                        dataRow.createCell(10).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }
                }
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, new int[] {256*10, 256*15, 256*15, 256*10, 256*10, 256*15, 256*15, 256*15, 256*15, 256*15, 256*15});

        } catch (Exception exception)
        {
            log.error("导出每日产出异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "每日产出报表.xlsx");
    }




    @ApiOperation(value = "导出单个项目报表Excel", notes = "导出单个项目报表Excel")
    @PostMapping(value = "/exportProductionProjectExcel")
    public void exportProductionProjectExcel(@Valid @RequestBody ProductionProjectReportQueryForm productionProjectReportQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", productionProjectReportQueryForm);
        List<String> productionDateList = productionReportService.findProductionReportDateByCondition(productionProjectReportQueryForm.getProjectName(),
                productionProjectReportQueryForm.getMold(),
                productionProjectReportQueryForm.getCycle(),
                productionProjectReportQueryForm.getDateStart(),
                productionProjectReportQueryForm.getDateEnd());


        List<Map<String, Object>> productionProjectList = productionReportService.queryProductionProjectReportByCondition(productionProjectReportQueryForm.toParam(ProductionProjectReportQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("单个项目报表");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("项目");
        titleRow.createCell(2).setCellValue("模具");
        titleRow.createCell(3).setCellValue("周期");
        titleRow.createCell(4).setCellValue("条件代码");
        titleRow.createCell(5).setCellValue("项目2");
        titleRow.createCell(6).setCellValue("汇总");
        if(productionDateList != null && productionDateList.size() > 0)
        {
            for(int i=0; i<productionDateList.size(); i++)
            {
                titleRow.createCell(7+i).setCellValue(productionDateList.get(i));
            }
        }

        List<String> percentCode = new ArrayList<>();
        percentCode.add("JHXNLIANGLV");
        percentCode.add("MBLIANGLV");
        percentCode.add("JHHDLIANGLV");
        percentCode.add("JHZHITONGLV");
        percentCode.add("SJXNLIANGLV");
        percentCode.add("SJHDLIANGLV");
        percentCode.add("SJLIANGLV");
        percentCode.add("SJZHITONGLV");
        percentCode.add("CHANCHUDCL");
        percentCode.add("RUKUDCL");

        try {
            if (productionProjectList != null && productionProjectList.size() > 0) {
                for (int i = 0; i < productionProjectList.size(); i++) {
                    Map<String, Object> productionDayMap = productionProjectList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    String projectName = productionDayMap.get("projectName") != null ? productionDayMap.get("projectName") + "" : "";
                    String mold = productionDayMap.get("mold") != null ? productionDayMap.get("mold") + "" : "";
                    String cycle = productionDayMap.get("cycle") != null ? productionDayMap.get("cycle") + "" : "";
                    String code = productionDayMap.get("code") != null ? productionDayMap.get("code") + "" : "";
                    String name = productionDayMap.get("name") != null ? productionDayMap.get("name") + "" : "";

                    dataRow.createCell(0).setCellValue(i + 1);
                    dataRow.createCell(1).setCellValue(projectName);
                    dataRow.createCell(2).setCellValue(mold);
                    dataRow.createCell(3).setCellValue(cycle);
                    dataRow.createCell(4).setCellValue(code);
                    dataRow.createCell(5).setCellValue(name);

                    if(productionDayMap.get("sumQty") != null)
                    {
                        if(percentCode.contains(code))
                        {
                            dataRow.createCell(6).setCellValue(productionDayMap.get("sumQty") + "");
                        }
                        else {
                            dataRow.createCell(6).setCellValue(Double.valueOf(productionDayMap.get("sumQty") + ""));
                        }
                    }else
                    {
                        dataRow.createCell(6).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }

                    if(productionDateList != null && productionDateList.size() > 0)
                    {
                        for(int j=0; j<productionDateList.size(); j++)
                        {
                            String productionDate = productionDateList.get(j);
                            if(productionDayMap.containsKey(productionDate) && productionDayMap.get(productionDate) != null) {
                                if(percentCode.contains(code))
                                {
                                    dataRow.createCell(7+j).setCellValue(productionDayMap.get(productionDate) + "");
                                }else
                                {
                                    dataRow.createCell(7+j).setCellValue(Double.valueOf(productionDayMap.get(productionDate) + ""));
                                }
                            }else{
                                dataRow.createCell(7+j).setCellType(XSSFCell.CELL_TYPE_BLANK);
                            }
                        }
                    }
                }
            }
            //调整列宽
            List<Integer> columnWidthList = new ArrayList<>();
            columnWidthList.add(256*10);
            columnWidthList.add(256*15);
            columnWidthList.add(256*10);
            columnWidthList.add(256*10);
            columnWidthList.add(256*15);
            columnWidthList.add(256*25);
            columnWidthList.add(256*10);
            for(int i=0; i<productionDateList.size(); i++)
            {
                columnWidthList.add(256*15);
            }

            ExcelUtil.setSheetColumnWidth(wbSheet, columnWidthList);

        } catch (Exception exception)
        {
            log.error("导出单个项目异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "单个项目报表.xlsx");
    }




    @ApiOperation(value = "导出月度汇总报表Excel", notes = "导出月度汇总报表Excel")
    @PostMapping(value = "/exportProductionMonthExcel")
    public void exportProductionMonthExcel(@Valid @RequestBody ProductionMonthReportQueryForm productionMonthReportQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", productionMonthReportQueryForm);
        List<String> productionDateList = productionReportService.findProductionReportDateByCondition(productionMonthReportQueryForm.getProjectName(),
                productionMonthReportQueryForm.getMold(),
                productionMonthReportQueryForm.getCycle(),
                productionMonthReportQueryForm.getDateStart(),
                productionMonthReportQueryForm.getDateEnd());


        List<Map<String, Object>> productionMonthList = productionReportService.queryProductionMonthReportByCondition(productionMonthReportQueryForm.toParam(ProductionMonthReportQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("月度汇总报表");

        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("项目");
        titleRow.createCell(2).setCellValue("条件代码");
        titleRow.createCell(3).setCellValue("项目2");
        titleRow.createCell(4).setCellValue("汇总");
        if(productionDateList != null && productionDateList.size() > 0)
        {
            for(int i=0; i<productionDateList.size(); i++)
            {
                titleRow.createCell(5+i).setCellValue(productionDateList.get(i));
            }
        }

        try {
            if (productionMonthList != null && productionMonthList.size() > 0) {
                for (int i = 0; i < productionMonthList.size(); i++) {
                    Map<String, Object> productionDayMap = productionMonthList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(i + 1);
                    dataRow.createCell(1).setCellValue(productionDayMap.get("projectName") != null ? productionDayMap.get("projectName") + "" : "");
                    dataRow.createCell(2).setCellValue(productionDayMap.get("code") != null ? productionDayMap.get("code") + "" : "");
                    dataRow.createCell(3).setCellValue(productionDayMap.get("name") != null ? productionDayMap.get("name") + "" : "");

                    if(productionDayMap.get("sumQty") != null){
                        if("RUKUDCL".equals(productionDayMap.get("code"))) {
                            dataRow.createCell(4).setCellValue(productionDayMap.get("sumQty") + "");
                        }else
                        {
                            dataRow.createCell(4).setCellValue(Double.valueOf(productionDayMap.get("sumQty") + ""));
                        }
                    }else
                    {
                        dataRow.createCell(4).setCellType(XSSFCell.CELL_TYPE_BLANK);
                    }


                    if(productionDateList != null && productionDateList.size() > 0)
                    {
                        for(int j=0; j<productionDateList.size(); j++)
                        {
                            String productionDate = productionDateList.get(j);
                            if("RUKUDCL".equals(productionDayMap.get("code")))
                            {
                                if(productionDayMap.containsKey(productionDate) && productionDayMap.get(productionDate) != null) {
                                    dataRow.createCell(5+j).setCellValue(productionDayMap.get(productionDate) + "");
                                }else{
                                    dataRow.createCell(5+j).setCellType(XSSFCell.CELL_TYPE_BLANK);
                                }
                            }
                            else
                            {
                                if(productionDayMap.containsKey(productionDate) && productionDayMap.get(productionDate) != null) {
                                    dataRow.createCell(5+j).setCellValue(Double.valueOf(productionDayMap.get(productionDate) + ""));
                                }else{
                                    dataRow.createCell(5+j).setCellType(XSSFCell.CELL_TYPE_BLANK);
                                }
                            }

                        }
                    }
                }
            }
            //调整列宽
            List<Integer> columnWidthList = new ArrayList<>();
            columnWidthList.add(256*10);
            columnWidthList.add(256*15);
            columnWidthList.add(256*15);
            columnWidthList.add(256*25);
            columnWidthList.add(256*15);
            for(int i=0; i<productionDateList.size(); i++)
            {
                columnWidthList.add(256*15);
            }
            ExcelUtil.setSheetColumnWidth(wbSheet, columnWidthList);
        } catch (Exception exception)
        {
            log.error("导出月度汇总异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "月度汇总报表.xlsx");
    }
}