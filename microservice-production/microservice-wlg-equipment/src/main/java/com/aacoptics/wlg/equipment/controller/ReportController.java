package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.DataDictConstants;
import com.aacoptics.wlg.equipment.entity.form.ExceptionTypeQueryForm;
import com.aacoptics.wlg.equipment.entity.form.MaintenanceOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.form.SectionSummaryOrderQueryForm;
import com.aacoptics.wlg.equipment.entity.param.ExceptionTypeQueryParam;
import com.aacoptics.wlg.equipment.entity.param.MaintenanceOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.vo.MaintenanceOrderAndItemVO;
import com.aacoptics.wlg.equipment.entity.vo.SectionSummaryOrderVO;
import com.aacoptics.wlg.equipment.service.ReportService;
import com.aacoptics.wlg.equipment.util.DataDictUtil;
import com.aacoptics.wlg.equipment.util.ExcelUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/report")
@Api("report")
@Slf4j
public class ReportController {

    @Autowired
    ReportService reportService;

    @ApiOperation(value = "搜索工段工单汇总", notes = "搜索工段工单汇总")
    @ApiImplicitParam(name = "sectionSummaryOrderQueryForm", value = "工段工单汇总查询参数", required = true, dataType = "SectionSummaryOrderQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/findSectionOrderCount")
    public Result findSectionOrderCount(@Valid @RequestBody SectionSummaryOrderQueryForm sectionSummaryOrderQueryForm) {
        log.debug("query with name:{}", sectionSummaryOrderQueryForm);
        return Result.success(reportService.findSectionOrderCount(sectionSummaryOrderQueryForm.toParam(SectionSummaryOrderQueryParam.class)));
    }


    @ApiOperation(value = "导出工段工单数统计Excel", notes = "导出工段工单数统计Excel")
    @PostMapping(value = "/exportSectionOrderCountExcel")
    public void exportSectionOrderCountExcel(@Valid @RequestBody SectionSummaryOrderQueryForm sectionSummaryOrderQueryForm, HttpServletResponse response) throws Exception {
        log.debug("query with name:{}", sectionSummaryOrderQueryForm);
        List<SectionSummaryOrderVO> sectionSummaryOrderVOList = reportService.findSectionOrderCount(sectionSummaryOrderQueryForm.toParam(SectionSummaryOrderQueryParam.class));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet wbSheet = workbook.createSheet("工段工单数统计");
        XSSFRow titleRow = wbSheet.createRow(0);
        titleRow.createCell(0).setCellValue("序号");
        titleRow.createCell(1).setCellValue("工段类型");
        titleRow.createCell(2).setCellValue("需点检工单数");
        titleRow.createCell(3).setCellValue("已提交点检工单数");
        titleRow.createCell(4).setCellValue("已确认点检工单数");
        titleRow.createCell(5).setCellValue("未完成点检工单数");
        titleRow.createCell(6).setCellValue("需保养工单数");
        titleRow.createCell(7).setCellValue("已提交保养工单数");
        titleRow.createCell(8).setCellValue("已确认保养工单数");
        titleRow.createCell(9).setCellValue("未完成保养工单数");
        titleRow.createCell(10).setCellValue("需维修工单数");
        titleRow.createCell(11).setCellValue("已提交维修工单数");
        titleRow.createCell(12).setCellValue("已确认维修工单数");
        titleRow.createCell(13).setCellValue("未完成维修工单数");

        try {
            if (sectionSummaryOrderVOList != null && sectionSummaryOrderVOList.size() > 0) {

                int rowNumber = 1;
                for (int i = 0; i < sectionSummaryOrderVOList.size(); i++) {
                    SectionSummaryOrderVO sectionSummaryOrderVO = sectionSummaryOrderVOList.get(i);
                    XSSFRow dataRow = wbSheet.createRow(rowNumber++);
                    dataRow.createCell(0).setCellValue(rowNumber - 1);
                    dataRow.createCell(1).setCellValue(sectionSummaryOrderVO.getSectionType() != null ? sectionSummaryOrderVO.getSectionType() + "" : "");

                    this.setNumberCellValue(dataRow, 2, sectionSummaryOrderVO.getInspectionOrderCount());
                    this.setNumberCellValue(dataRow, 3, sectionSummaryOrderVO.getCommittedInspectionOrderCount());
                    this.setNumberCellValue(dataRow, 4, sectionSummaryOrderVO.getConfirmedInspectionOrderCount());
                    this.setNumberCellValue(dataRow, 5, sectionSummaryOrderVO.getUnfinishedInspectionOrderCount());

                    this.setNumberCellValue(dataRow, 6, sectionSummaryOrderVO.getMaintenanceOrderCount());
                    this.setNumberCellValue(dataRow, 7, sectionSummaryOrderVO.getCommittedMaintenanceOrderCount());
                    this.setNumberCellValue(dataRow, 8, sectionSummaryOrderVO.getConfirmedMaintenanceOrderCount());
                    this.setNumberCellValue(dataRow, 9, sectionSummaryOrderVO.getUnfinishedMaintenanceOrderCount());

                    this.setNumberCellValue(dataRow, 10, sectionSummaryOrderVO.getRepairOrderCount());
                    this.setNumberCellValue(dataRow, 11, sectionSummaryOrderVO.getCommittedRepairOrderCount());
                    this.setNumberCellValue(dataRow, 12, sectionSummaryOrderVO.getConfirmedRepairOrderCount());
                    this.setNumberCellValue(dataRow, 13, sectionSummaryOrderVO.getUnfinishedRepairOrderCount());
                }
            }
            ExcelUtil.setSheetColumnWidth(wbSheet, new int[]{256 * 10, 256 * 15, 256 * 15, 256 * 20, 256 * 20, 256 * 20, 256 * 15, 256 * 20, 256 * 20, 256 * 20, 256 * 15, 256 * 20, 256 * 20, 256 * 20});

        } catch (Exception exception) {
            log.error("导出工段工单数统计异常", exception);
            throw exception;
        }

        ExcelUtil.exportXlsx(response, workbook, "工段工单数统计.xlsx");
    }

    private void setNumberCellValue(XSSFRow dataRow, int columnIndex, Object value) {
        if (value != null) {
            dataRow.createCell(columnIndex).setCellValue(Double.valueOf(value + ""));
        } else {
            dataRow.createCell(columnIndex).setCellType(CellType.BLANK);
        }
    }

}