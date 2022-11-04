package com.aacoptics.data.analysis.util;


import com.aacoptics.data.analysis.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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
         *
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
         *
         * @param wb
         * @return
         */
        void handle(Workbook wb);
    }

    /**
     * 读取excel指定sheet数据<br>未关闭io
     *
     * @param in
     */
    public static void readSheet(InputStream in, int sheetIdx, SheetHandler handler) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);
        // 接口回调处理
        handler.handle(getSheetByIdx(wb, sheetIdx));
    }

    /**
     * 获取workbook
     *
     * @param in
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook getWorkbook(InputStream in) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);
        return wb;
    }


    /**
     * 读取excel全部sheet全部行数据<br>未关闭io
     *
     * @param wb
     * @return List<List < String [ ]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<List<String[]>> read(Workbook wb) throws IOException, InvalidFormatException {
        // 默认读取所有行，所有列
        return readExcel(wb);
    }

    /**
     * 读取excel全部sheet全部行数据<br>未关闭io
     *
     * @param in
     * @return List<List < String [ ]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<String[]> readBySheetNum(InputStream in, int sheetNum) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);
        // 默认读取所有行，所有列
        return readExcelBySheetNum(wb, sheetNum);
    }

    /**
     * 读取excel<br>自定义解析方式<br>未关闭io
     *
     * @param in
     * @return
     */
    public static void readByCustomize(InputStream in, WorkBookHandler handler) throws IOException, InvalidFormatException {
        // 创建工作簿
        Workbook wb = createWorkBook(in);

        handler.handle(wb);
    }

    /**
     * 读取指定sheet的范围行，范围列
     *
     * @param sheet    excel 某个sheet
     * @param startRow 开始行下标
     * @param endRow   结束行下标
     * @param startCol 开始列下标
     * @param endCol   结束列下标
     * @return 指定范围的数据。一个String[]是一行数据
     */
    public static List<String[]> readSheetPart(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {

        if (sheet == null) {
            logger.warn("excel sheet is not exist");
            return null;
        }

        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        logger.info("sheet firstRow:" + firstRow);
        logger.info("sheet lastRow:" + lastRow);
        if (startRow < 0 || startRow > lastRow || startRow > endRow) {
            return null;
        }
        if (endRow > lastRow) {
            return null;
        }

        List<String[]> list = new ArrayList<>();
        // 逐行解析
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            // 过滤空行
            if (row == null) {
                continue;
            }
            int firstCol = row.getFirstCellNum();
            int lastCol = row.getLastCellNum();
            logger.info("row(" + i + ") firstCol:" + firstCol);
            logger.info("row(" + i + ") lastCol:" + lastCol);
            if (startCol < 0 || startCol > lastCol || startCol > endCol) {
                return null;
            }
            if (endCol > lastCol) {
                return null;
            }

            int colIdx = 0;
            int objIdx = 0;
            String[] objs = new String[endCol - startCol + 1];
            // 逐列解析
            for (Cell c : row) {
                // 读取指定列数据
                if (colIdx >= startCol && colIdx <= endCol) {
                    c.setCellType(CellType.STRING);
                    boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                    String data = "";
                    //判断是否具有合并单元格
                    if (isMerge) {
                        data = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    } else {
                        data = c.getRichStringCellValue().toString();
                    }
                    objs[objIdx++] = data;
                }
                colIdx++;
            }
            list.add(objs);
        }

        if (list.size() == 0) {
            logger.warn("excel sheet data is space");
        }

        return list;
    }

    /**
     * 读取excel文件
     *
     * @param wb
     * @return List<List < String [ ]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<String[]> readExcelBySheetNum(Workbook wb, int sheetNum) {
        // 总sheet数
        int workbookSheetNum = wb.getNumberOfSheets();
        logger.info("excel sheetNum is :" + sheetNum);
        if (workbookSheetNum < sheetNum) {
            logger.error("sheetNum" + sheetNum + "不能大于总excel总sheet数" + workbookSheetNum);
            return null;
        }
        // 遍历sheet
        Sheet sheet = wb.getSheetAt(sheetNum);
        List<String[]> sheetData = readOneSheet(sheet);

        return sheetData;
    }

    /**
     * 读取excel文件
     *
     * @param wb
     * @return List<List < String [ ]>> <br>List<String[]>表示第i个sheet<br> String[]表示sheet某行
     */
    public static List<List<String[]>> readExcel(Workbook wb) {

        // 总sheet数
        int sheetNum = wb.getNumberOfSheets();
        logger.info("excel sheetNum is :" + sheetNum);

        // 遍历sheet
        List<List<String[]>> list = new ArrayList<>();
        for (int i = 0; i < sheetNum; i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<String[]> sheetData = readOneSheet(sheet);
            list.addAll(Collections.singleton(sheetData));
        }

        return list;
    }

    /**
     * 读取单个sheet数据，封装为List<String[]><br>
     *
     * @return List<String [ ]>,一个String[]是
     * @warn 可能会有全是空串的数据
     */
    public static List<String[]> readOneSheet(Sheet sheet) {
        int startRow = sheet.getFirstRowNum();
        int endRow = sheet.getLastRowNum();
        logger.info("sheet startRow:" + startRow);
        logger.info("sheet endRow:" + endRow);
        List<String[]> list = new ArrayList<>();
        // 遍历行
        StringBuffer errorMessage = new StringBuffer();
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            // int startColIdx = row.getFirstCellNum();
            int endColIdx = sheet.getRow(startRow).getLastCellNum();
            String[] objs = new String[endColIdx + 1];
            int colIdx = 0;
            // 遍历列
            for (int j = 0; j < endColIdx + 1; j++) {
                Cell c = row.getCell(j);
                String data = "";
                if (c == null) {
                    data = "";
                } else {
                    try {
//                    c.setCellType(Cell.CELL_TYPE_STRING);
                        boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                        //判断是否具有合并单元格
                        if (isMerge) {
                            data = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                        } else {
                            data = getCellValue(c);
//                        data = c.getRichStringCellValue().toString();
                        }
                    } catch (Exception err) {
                        logger.error("第{}行{}列读取值异常：{}", i + 1, c.getColumnIndex() + 1, err.getMessage());
                        errorMessage.append("第" + (i + 1) + "行 " + (c.getColumnIndex() + 1) + "列读取值异常：" + err.getMessage() + ";");
                    }
                }
                objs[colIdx] = data;
                colIdx++;
            }
            list.add(objs);
        }
        if (StringUtils.isNotEmpty(errorMessage.toString())) {
            throw new BusinessException(errorMessage.toString());
        }

        if (list.size() == 0) {
            logger.warn("excel sheet data is space");
        }

        return list;
    }

    /**
     * 获取合并单元格的值<br>
     * 即获取合并单元格第一个cell的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {

        // 获得一个 sheet 中合并单元格的数量
        int sheetMergeCount = sheet.getNumMergedRegions();

        // 遍历合并单元格
        for (int i = 0; i < sheetMergeCount; i++) {

            // 得出具体的合并单元格
            CellRangeAddress ca = sheet.getMergedRegion(i);

            // 得到合并单元格的起始行, 结束行, 起始列, 结束列
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            // 获取合并单元格第一个cell的值
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {

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
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     *
     * @param sheet
     * @return
     */
    public static boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType().getCode()) {
            case 1:
                return cell.getStringCellValue();
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 2:
//                return cell.getCellFormula() ;
                String value = null;
                try {
                    value = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    value = String.valueOf(cell.getRichStringCellValue());
                }
                return value;
            case 0:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                            .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    String result = sdf.format(date);
                    return result;
                } else {
                    cell.setCellType(CellType.STRING);
                    return cell.getRichStringCellValue() != null ? String.valueOf(cell.getRichStringCellValue()) : "";
                }
            default:
                return "";
        }
    }

    /**
     * 创建工作簿
     *
     * @param in
     */
    private static Workbook createWorkBook(InputStream in) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(in);
        return wb;
    }

    /**
     * 获取sheet
     *
     * @param wb
     * @param sheetIdx
     */
    public static Sheet getSheetByIdx(Workbook wb, int sheetIdx) {

        // 总sheet数
        int sheetNum = wb.getNumberOfSheets();

        // sheet不存在
        if (sheetIdx < 0 || sheetIdx >= sheetNum) {
            return null;
        }

        // 获取sheet
        Sheet sheet = wb.getSheetAt(sheetIdx);

        return sheet;
    }


    /*----------写excel相关----------*/

    /**
     * 添加合并单元格
     *
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    public static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }


    /**
     * 导出excel对象
     *
     * @param response httpResponse对象
     * @param workbook workBook对象
     * @param fileName 导出文件名
     * @throws Exception 异常抛出
     */
    public static void exportXlsx(HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition",
                "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xlsx");
        ServletOutputStream outStream = null;
        try {
            outStream = response.getOutputStream();
            workbook.write(outStream);
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    /**
     * 设置表格列宽度
     *
     * @param sheet
     * @param columnWidthArray
     */
    public static void setSheetColumnWidth(XSSFSheet sheet, int[] columnWidthArray) {
        for (int i = 0; i < columnWidthArray.length; i++) {
            sheet.setColumnWidth(i, columnWidthArray[i]);
        }
    }

    /**
     * 设置表格列宽度
     *
     * @param sheet
     * @param columnWidthList
     */
    public static void setSheetColumnWidth(XSSFSheet sheet, List<Integer> columnWidthList) {
        for (int i = 0; i < columnWidthList.size(); i++) {
            sheet.setColumnWidth(i, columnWidthList.get(i));
        }
    }

    /**
     * 有效数字处理
     *
     * @param str
     * @param number
     * @return
     */
    public static String handleDecimal(String str, int number) {
        double decimal = Double.valueOf(str);
        String format = str;
        // 取整
        if (number == 0) {
            long i = Math.round(decimal);
            format = String.valueOf(i);
        } else {

            format = String.format("%." + number + "f", decimal);
        }
        return format;
    }

    /**
     * 获取图片
     *
     * @param wb
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures(Workbook wb) throws IOException {
        XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);
        Map<String, PictureData> map = new HashMap<>();
        List<POIXMLDocumentPart> relations = sheet.getRelations();
        for (POIXMLDocumentPart part : relations) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker from = anchor.getFrom();
                    // int rowNum = from.getRow();
                    // int colNum = from.getCol();
                    String key = from.getRow() + "_" + from.getCol();
                    // System.out.println(rowNum + "-------" + colNum);
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }

    /**
     * 删除文件，文件重复写入需要替换
     *
     * @param fileName 文件名
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 图片保存
     *
     * @param sheetList
     * @param filePathPrefix
     * @return
     * @throws Exception
     */
    public static Map<String, String> saveImg(Map<String, PictureData> sheetList, String filePathPrefix) throws Exception {
        Map<String, String> map = new HashMap<>();
        FtpUtil.connect();
        FtpUtil.changeWorkingDirectory(filePathPrefix);
        for (Map.Entry<String, PictureData> entry : sheetList.entrySet()) {
            // 获取图片流
            PictureData pic = entry.getValue();
            // 获取图片索引
            String picName = entry.getKey() + "_" + (new Date()).getTime();
            // 获取图片格式
            String ext = pic.suggestFileExtension();
            map.put(entry.getKey(), picName + "." + ext);
            FtpUtil.uploadFile(new ByteArrayInputStream(pic.getData()), picName);
        }
        return map;
    }

    /**
     * 将图片导入到Excel
     *
     * @param wb
     * @param filePathPrefix
     * @param fileNameWithExt
     * @param row
     * @param col
     * @param scaleX
     * @param scaleY
     */
    public static void exportImg(Workbook wb, XSSFSheet sheet, String filePathPrefix, String fileNameWithExt, int row, int col, double scaleX, double scaleY) {
        try {
            String fileName = "";
            String ext = "";
            if (StringUtils.isNotEmpty(fileNameWithExt) && fileNameWithExt.indexOf(".") != -1) {
                String[] split = fileNameWithExt.split("\\.");
                fileName = split[0];
                ext = split[1];
            }
            FtpUtil.connect();
            FtpUtil.changeWorkingDirectory(filePathPrefix);
            InputStream inputStream = FtpUtil.getInputStream(fileName);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = 0;
            if (ext.equals("png")) {
                pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_PNG);
            } else {
                pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_JPEG);
            }
            CreationHelper helper = wb.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            // 图片插入坐标
            anchor.setCol1(col);
            anchor.setRow1(row);

            Drawing drawing = sheet.createDrawingPatriarch();
            // 插入图片
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(scaleX, scaleY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}