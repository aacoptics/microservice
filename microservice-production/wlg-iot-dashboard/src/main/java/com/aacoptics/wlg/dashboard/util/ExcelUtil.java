package com.aacoptics.wlg.dashboard.util;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author Kaizhi Xuan
 * created by 2022/4/13 10:28
 */
@SuppressWarnings("DuplicatedCode")
public class ExcelUtil {

    public static void createCell(Row row, int cellId, Object name, CellStyle cellStyle) {
        Cell cell = row.createCell(cellId);
        if (name instanceof String) cell.setCellValue((String) name);

        if (name instanceof Long) cell.setCellValue((Long) name);

        if (name instanceof Double) cell.setCellValue((Double) name);

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Workbook workbook, Row row, int cellId, Object name, Sheet styleSheet, int styleRowId, int styleCellId) {
        Cell cell = row.createCell(cellId);
        if (name instanceof String) cell.setCellValue((String) name);

        if (name instanceof Long) cell.setCellValue((Long) name);

        if (name instanceof Double) cell.setCellValue((Double) name);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.cloneStyleFrom(styleSheet.getRow(styleRowId).getCell(styleCellId).getCellStyle());
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);
    }

    public static void updateBorder(Workbook workbook) {
        IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetAt).forEach(sheet ->  // 循环sheet
                IntStream.range(0, sheet.getLastRowNum() + 1)
                        .mapToObj(sheet::getRow)
                        .filter(ObjectUtil::isNotNull)
                        .forEach(row -> IntStream.range(0, row.getLastCellNum()).mapToObj(row::getCell).filter(ObjectUtil::isNotNull).forEach(cell -> {
                            final CellStyle cellStyle = cell.getCellStyle();
                            setCellBorderStyle(cellStyle);
                        })));
    }

    public static void updateAutoWidth(Workbook workbook) {
        IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetAt).forEach(sheet ->
                IntStream.range(0, sheet.getLastRowNum() + 1)
                        .mapToObj(sheet::getRow)
                        .filter(ObjectUtil::isNotNull)
                        .forEach(row ->  // 循环row
                                IntStream.range(0, row.getLastCellNum()).forEach(colNumber -> {
                                    sheet.autoSizeColumn(colNumber);
                                    sheet.setColumnWidth(colNumber, sheet.getColumnWidth(colNumber) * 17 / 10);
                                })));
    }

    public static CellStyle getBaseCellStyle(Workbook workbook) { // 基础样式
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN); // 下边框
        style.setBorderLeft(BorderStyle.THIN); // 左边框
        style.setBorderTop(BorderStyle.THIN); // 上边框
        style.setBorderRight(BorderStyle.THIN); // 右边框
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 上下居中
        style.setWrapText(true); // 设置自动换行
        return style;
    }

    public static Font getFont(Workbook workbook, short fontSize, boolean isBold) {
        Font font = workbook.createFont();
        font.setFontName("宋体"); // 字体样式
        font.setBold(isBold); // 是否加粗
        font.setFontHeightInPoints(fontSize); // 字体大小
        return font;
    }

    /**
     * 编码文件名
     */
    public static String encodingDocFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".docx";
        return filename;
    }

    /**
     * 编码文件名
     */
    public static String encodingExcelFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    public static void exportXlsx(HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1) + ".xlsx");
        try (ServletOutputStream outStream = response.getOutputStream()) {
            workbook.write(outStream);
        }
    }

    public static Map<String, Object> objectToExcelExportParam(String title, String sheetName, Object object, Class<?> clazz) {
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyleUtil.class);
        exportParams.setTitle(title);
        exportParams.setSheetName(sheetName);

        return ImmutableMap.<String, Object>builder()
                .put("title", exportParams)
                .put(NormalExcelConstants.CLASS, clazz)
                .put(NormalExcelConstants.DATA_LIST, object)
                .build();
    }

    public static void freezeRow(Workbook workbook, int rowNumber) {
        IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetAt).forEach(sheet ->  // 循环sheet
                sheet.createFreezePane(0, rowNumber + 1, 0, rowNumber + 1));
    }

    public static void AutoFilterRow(Workbook workbook) {
        IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetAt).forEach(sheet ->  // 循环sheet
        {
            //首行加筛选框
            CellRangeAddress c = new CellRangeAddress(0, sheet.getLastRowNum() - 1, 0, sheet.getRow(0).getLastCellNum() - 1);
            //这个sheet是文档当前页，一个对象
            sheet.setAutoFilter(c);
        });
    }

    public static void setCellBorderStyle(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); // 左边框
        cellStyle.setBorderTop(BorderStyle.THIN); // 上边框
        cellStyle.setBorderRight(BorderStyle.THIN); // 右边框
    }

    public static void setCellCommonStyle(CellStyle cellStyle) {
        setCellBorderStyle(cellStyle);
        cellStyle.setAlignment(HorizontalAlignment.LEFT); // 水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 上下居中
        cellStyle.setWrapText(true); // 设置自动换行
    }
}
