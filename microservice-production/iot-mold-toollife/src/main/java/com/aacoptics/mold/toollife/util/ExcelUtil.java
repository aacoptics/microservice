package com.aacoptics.mold.toollife.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 对excel单个sheet进行处理的接口
     */
    public interface SheetHandler {
        /**
         * 对sheet进行处理
         * @param sheet
         */
        void handle(Sheet sheet);
    }

    /**
     * 对excel进行处理的接口
     */
    public interface WorkBookHandler {
        /**
         * 对excel进行处理
         * @param wb
         * @return
         */
        void handle(Workbook wb);
    }

    /**
     * 读取excel指定sheet数据<br>未关闭io
     * @param  in
     */
    public static void readSheet(InputStream in, int sheetIdx, SheetHandler handler) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);
        // 接口回调处理
        handler.handle(getSheetByIdx(wb, sheetIdx));
    }


    /**
     * 读取excel全部sheet全部行数据<br>未关闭io
     * @param  in
     * @return List<List<String[]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<List<String[]>> read(InputStream in) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);
        // 默认读取所有行，所有列
        return readExcel(wb);
    }

    /**
     * 读取excel<br>自定义解析方式<br>未关闭io
     * @param  in
     * @return
     */
    public static void readByCustomize(InputStream in, WorkBookHandler handler) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);

        handler.handle(wb);
    }

    /**
     * 读取指定sheet的范围行，范围列
     * @param sheet excel 某个sheet
     * @param startRow 开始行下标
     * @param endRow 结束行下标
     * @param startCol 开始列下标
     * @param endCol 结束列下标
     * @return 指定范围的数据。一个String[]是一行数据
     */
    public static List<String[]> readSheetPart(Sheet sheet, int startRow, int endRow, int startCol, int endCol){

        if(sheet == null){
            logger.warn("excel sheet is not exist");
            return null;
        }

        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        logger.info("sheet firstRow:"+firstRow);
        logger.info("sheet lastRow:"+lastRow);
        if(startRow < 0 || startRow > lastRow || startRow > endRow){
            return null;
        }
        if(endRow > lastRow){
            return null;
        }

        List<String[]> list = new ArrayList<>();
        // 逐行解析
        for (int i = startRow; i <= endRow ; i++) {
            Row row = sheet.getRow(i);
            // 过滤空行
            if(row == null){
                continue;
            }
            int firstCol = row.getFirstCellNum();
            int lastCol = row.getLastCellNum();
            logger.info("row("+i+") firstCol:"+firstCol);
            logger.info("row("+i+") lastCol:"+lastCol);
            if(startCol < 0 || startCol > lastCol || startCol > endCol){
                return null;
            }
            if(endCol > lastCol){
                return null;
            }

            int colIdx = 0;
            int objIdx = 0;
            String[] objs = new String[endCol-startCol+1];
            // 逐列解析
            for (Cell c : row) {
                // 读取指定列数据
                if(colIdx >= startCol && colIdx <= endCol){
                    c.setCellType(Cell.CELL_TYPE_STRING);
                    boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                    String data = "";
                    //判断是否具有合并单元格
                    if(isMerge) {
                        data = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    }else {
                        data = c.getRichStringCellValue().toString();
                    }
                    objs[objIdx++]=data;
                }
                colIdx++;
            }
            list.add(objs);
        }

        if(list.size() == 0){
            logger.warn("excel sheet data is space");
        }

        return list;
    }

    /**
     * 读取excel文件
     * @param  wb
     * @return List<List<String[]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<List<String[]>> readExcel(Workbook wb) {

        // 总sheet数
        int sheetNum = wb.getNumberOfSheets();
        logger.info("excel sheetNum is :"+sheetNum);

        // 遍历sheet
        List<List<String[]>> list = new ArrayList<>();
        for (int i = 0; i < sheetNum ; i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<String[]> sheetData = readOneSheet(sheet);
            list.addAll(Collections.singleton(sheetData));
        }

        return list;
    }

    /**
     * 读取单个sheet数据，封装为List<String[]><br>
     * @warn 可能会有全是空串的数据
     * @return List<String[]>,一个String[]是
     */
    public static List<String[]> readOneSheet(Sheet sheet){
        int startRow = sheet.getFirstRowNum();
        int endRow = sheet.getLastRowNum();
        logger.info("sheet startRow:"+startRow);
        logger.info("sheet endRow:"+endRow);
        List<String[]> list = new ArrayList<>();
        // 遍历行
        for (int i = startRow; i <= endRow ; i++) {
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            // int startColIdx = row.getFirstCellNum();
            int endColIdx = row.getLastCellNum();
            String[] objs = new String[endColIdx+1];
            int colIdx = 0;
            // 遍历列
            for (Cell c : row) {
                if(c == null)
                    continue;
                if(colIdx > 22)
                    break;
                String data = "";
                try {
                    c.setCellType(Cell.CELL_TYPE_STRING);
                    boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                    //判断是否具有合并单元格
                    if (isMerge) {
                        data = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    } else {
                        data = c.getRichStringCellValue().toString();
                    }
                }catch(Exception err){
                    //logger.error(err.getMessage());
                }
                objs[colIdx]=data;
                colIdx++;
            }
            list.add(objs);
        }

        if(list.size() == 0){
            logger.warn("excel sheet data is space");
        }

        return list;
    }

    /**
     * 获取合并单元格的值<br>
     * 即获取合并单元格第一个cell的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){

        // 获得一个 sheet 中合并单元格的数量
        int sheetMergeCount = sheet.getNumMergedRegions();

        // 遍历合并单元格
        for(int i = 0 ; i < sheetMergeCount ; i++){

            // 得出具体的合并单元格
            CellRangeAddress ca = sheet.getMergedRegion(i);

            // 得到合并单元格的起始行, 结束行, 起始列, 结束列
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            // 获取合并单元格第一个cell的值
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet,int row ,int column) {

        // 得到一个sheet中有多少个合并单元格
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {

            // 得出具体的合并单元格
            CellRangeAddress range = sheet.getMergedRegion(i);

            // 得到合并单元格的起始行, 结束行, 起始列, 结束列
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();

            // 判断该单元格是否在合并单元格范围之内, 如果是, 则返回 true
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     * @return
     */
    public static boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){
        if(cell == null) return "";
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula() ;
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    /**
     * 创建工作簿
     * @param in
     */
    private static Workbook createWorkBook(InputStream in) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(in);
        return wb;
    }

    /**
     * 获取sheet
     * @param wb
     * @param sheetIdx
     */
    public static Sheet getSheetByIdx(Workbook wb, int sheetIdx){

        // 总sheet数
        int sheetNum = wb.getNumberOfSheets();

        // sheet不存在
        if(sheetIdx < 0 || sheetIdx >= sheetNum){
            return null;
        }

        // 获取sheet
        Sheet sheet = wb.getSheetAt(sheetIdx);

        return sheet;
    }


    /*----------写excel相关----------*/
    /**
     * 添加合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    public static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
}