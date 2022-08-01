package com.aacoptics.wlg.report.service.impl;

import com.aacoptics.wlg.report.entity.dingtalk.message.MarkdownGroupMessage;
import com.aacoptics.wlg.report.entity.po.DingTalkMessageHistory;
import com.aacoptics.wlg.report.mapper.DingTalkNotificationMapper;
import com.aacoptics.wlg.report.provider.DingTalkApi;
import com.aacoptics.wlg.report.service.DingTalkNotificationService;
import com.aacoptics.wlg.report.service.ProductionReportService;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DingTalkNotificationServiceImpl implements DingTalkNotificationService {

    private static final String CURRENT_DATE_FORMAT = "M月d日";


    @Autowired
    ProductionReportService productionReportService;

    @Autowired
    DingTalkNotificationMapper dingTalkNotificationMapper;

    @Resource
    DingTalkApi dingTalkApi;


    @Override
    public void sendProductionDayDataNotification(String groupType) throws ApiException {
        LocalDateTime currentTime = LocalDateTime.now().minusDays(1);
        //获取当月一号
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);


        String currentDate = currentTime.format(DateTimeFormatter.ofPattern(CURRENT_DATE_FORMAT));

        //1 获取数据
        List<Map<String, Object>> productionDataList = productionReportService.queryProductionDayDataByDate(monthStart.toLocalDate(),
                currentTime.toLocalDate());
        if(productionDataList == null || productionDataList.size() == 0)
        {
            log.info("没有需要推送到钉钉群的数据");
            return;
        }
        //2 获取机器人
        List<Map<String, String>> robotList = dingTalkNotificationMapper.findRobotListByType(groupType);
        if(robotList == null || robotList.size() == 0)
        {
            log.info("没有维护机器人");
            return;
        }


        //3 构建markdown消息格式
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("WLG项目汇总产出数据（" + currentDate + "）");

        for(Map<String, Object> productionData: productionDataList)
        {
            String projectName = productionData.get("project_name") != null ? productionData.get("project_name")+"" : "";
            String product = productionData.get("product") != null ? productionData.get("product")+"" : "";
            String sumAfterOutputQty = productionData.get("sum_after_output_qty") != null ? decimalFormat.format(new BigDecimal(productionData.get("sum_after_output_qty")+"")) : ""; //实际累计产出（颗）
            String sumInventoryQty = productionData.get("sum_inventory_qty") != null ? decimalFormat.format(new BigDecimal(productionData.get("sum_inventory_qty")+"")) : ""; //实际累计入库（颗）
            String afterOutputQty = productionData.get("after_output_qty") != null ? decimalFormat.format(new BigDecimal(productionData.get("after_output_qty")+"")) : ""; //实际外观检产出（颗）
            String inventoryQty = productionData.get("inventory_qty") != null ? decimalFormat.format(new BigDecimal(productionData.get("inventory_qty")+"")) : ""; //实际转镀膜入库（颗）
            String moldPressOutputQty = productionData.get("mold_press_output_qty") != null ? decimalFormat.format(new BigDecimal(productionData.get("mold_press_output_qty")+"")) : ""; //实际生产模数（模）
            String sumJHTouRu = productionData.get("SUM_JHTOURU") != null ? decimalFormat.format(new BigDecimal(productionData.get("SUM_JHTOURU")+"")) : ""; //
            String sumJHLeiJi = productionData.get("SUM_JHLEIJI") != null ? decimalFormat.format(new BigDecimal(productionData.get("SUM_JHLEIJI")+"")) : ""; //计划累计产出（颗）
            String jhTouRu = productionData.get("JHTOURU") != null ? decimalFormat.format(new BigDecimal(productionData.get("JHTOURU")+"")) : ""; //计划生产模数（模）
            String jhhdChanChu = productionData.get("JHHDCHANCHU") != null ? decimalFormat.format(new BigDecimal(productionData.get("JHHDCHANCHU")+"")) : ""; //计划产出（颗）
            String yuGuMoYa = productionData.get("YUGUMOYA") != null ? decimalFormat.format(new BigDecimal(productionData.get("YUGUMOYA")+"")) : ""; //预估模压产出（颗）
            String chanChuRate = productionData.get("CHANCHU_RATE") != null ? productionData.get("CHANCHU_RATE")+"" : ""; //实际产出达成率(实际外观检产出/计划产出）
            String leiJiRate = productionData.get("LEIJI_RATE") != null ? productionData.get("LEIJI_RATE")+"" : ""; //实际累计产出达成率（实际累计产出/计划累计产出）


            if("汇总".equals(projectName))
            {
                markdownGroupMessage.addBlobContent("1、单日产出数据");
                markdownGroupMessage.addContent("计划产出（颗）：" + jhhdChanChu);
                markdownGroupMessage.addContent("实际外观检产出（颗）：" + afterOutputQty);
                markdownGroupMessage.addContent("实际转镀膜入库（颗）：" + inventoryQty);
                markdownGroupMessage.addContent("实际产出达成率(实际外观检产出/计划产出）：" + chanChuRate);
                markdownGroupMessage.addBlobContent("2、累计产出数据");
                markdownGroupMessage.addContent("计划累计产出（颗）：" + sumJHLeiJi);
                markdownGroupMessage.addContent("实际累计产出（颗）：" + sumAfterOutputQty);
                markdownGroupMessage.addContent("实际累计入库（颗）：" + sumInventoryQty);
                markdownGroupMessage.addContent("实际累计产出达成率（实际累计产出/计划累计产出）：" + leiJiRate);
                markdownGroupMessage.addBlankLine();
            }
            else
            {
                if(StringUtils.isEmpty(product))
                {
                    markdownGroupMessage.addBlobContent("项目：" + projectName);
                }else
                {
                    markdownGroupMessage.addBlobContent("项目：" + projectName + "（" + product +"）");
                }
                markdownGroupMessage.addBlobContent("1、单日产出数据");
                markdownGroupMessage.addContent("计划生产模数（模）：" + jhTouRu);
                markdownGroupMessage.addContent("实际生产模数（模）：" + moldPressOutputQty);
                markdownGroupMessage.addContent("计划产出(颗)：" + jhhdChanChu);
                markdownGroupMessage.addContent("预估模压产出（颗）：" + yuGuMoYa);
                markdownGroupMessage.addContent("实际外观检产出（颗）：" + afterOutputQty);
                markdownGroupMessage.addContent("实际转镀膜入库（颗）：" + inventoryQty);
                markdownGroupMessage.addContent("实际产出达成率(实际外观检产出/计划产出）：" + chanChuRate);
                markdownGroupMessage.addBlobContent("2、累计产出数据");
                markdownGroupMessage.addContent("计划累计产出（颗）：" + sumJHLeiJi);
                markdownGroupMessage.addContent("实际累计产出（颗）：" + sumAfterOutputQty);
                markdownGroupMessage.addContent("实际累计入库（颗）：" + sumInventoryQty);
                markdownGroupMessage.addContent("实际累计产出达成率（实际累计产出/计划累计产出）：" + leiJiRate);
            }
        }

        log.info("WLG推送消息内容" + markdownGroupMessage.toString());
        //4 发送消息
        DingTalkMessageHistory dingTalkMessageHistory = new DingTalkMessageHistory();
        dingTalkMessageHistory.setProductionDate(currentTime.toLocalDate());

        for(Map<String, String> robotMap : robotList) {
            String robotId = robotMap.get("ROBOT_ID");
            String robotUrl = robotMap.get("ROBOT_URL");

            Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, "WLG项目汇总产出数据", markdownGroupMessage.toString());
            String result = resultMap.get("result");
            String message = resultMap.get("message");
            //5 保存推送历史
            if (!StringUtils.isEmpty(message) && message.length() > 1024) {
                message = message.substring(1024);
            }
            dingTalkMessageHistory.setRobotId(robotId);
            dingTalkMessageHistory.setResult(result);
            dingTalkMessageHistory.setMessage(message);

            dingTalkNotificationMapper.insert(dingTalkMessageHistory);
        }

    }

    @Override
    public void sendWLGProductionDayDataImageNotification(String groupType) throws ApiException {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime currentTime = nowTime.minusDays(1);
        //获取当月一号
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);

        int month = currentTime.getMonthValue();
        int monthDay = currentTime.getDayOfMonth();


        String currentDate = currentTime.format(DateTimeFormatter.ofPattern(CURRENT_DATE_FORMAT));
        //1 获取数据
        List<Map<String, Object>> productionDataList = productionReportService.findProductionDayReportDataByDate(monthStart.toLocalDate(), monthEnd.toLocalDate(),
                currentTime.toLocalDate());
        if(productionDataList == null || productionDataList.size() == 0)
        {
            log.info("没有需要推送到钉钉群的数据");
            return;
        }
        //获取客户需求数据
        List<Map<String, Object>> customerRequirementDataList = null;
        if(productionDataList != null && productionDataList.size() >0)
        {
            customerRequirementDataList = productionReportService.findCustomerRequirementDataByDate(monthStart.toLocalDate(),
                    currentTime.toLocalDate());
        }
        //获取当日数据
        List<Map<String, Object>> productionCurrentDayDataList = null;
        if(productionDataList != null && productionDataList.size() >0)
        {
            productionCurrentDayDataList = productionReportService.findProductionCurrentDayDataByDate(currentTime.toLocalDate());
        }

        //2 获取机器人
        List<Map<String, String>> robotList = dingTalkNotificationMapper.findRobotListByType(groupType);
        if(robotList == null || robotList.size() == 0)
        {
            log.info("没有维护机器人");
            return;
        }

        //3 导出成Excel
        //excel模板路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/WLG产出报表模板.xlsx");

        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "WLG生产报表-" + currentTimeMillis + ".xlsx";
        String imageFileName = "WLG生产报表-" + currentTimeMillis + ".png";

        int productionDataSize = productionDataList.size();
        //读取excel模板
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);

            //读取了模板内所有sheet内容
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //替换表头中的日期
            XSSFRow titleRow = sheet.getRow(0);
            for(int i=3; i<=13; i++)
            {
                XSSFCell titleCell = titleRow.getCell(i);
                String cellValue = titleCell.getStringCellValue();
                String resultCellValue = cellValue.replace("当月", month+"月").replace("当日", currentDate);
                titleCell.setCellValue(resultCellValue);
            }


            //如果这行没有了，整个公式都不会有自动计算的效果的
//            sheet.setForceFormulaRecalculation(true);
            for(int i=0; i<productionDataSize; i++)
            {
                Map<String, Object> productionDayMap = productionDataList.get(i);
                XSSFRow dataRow = sheet.createRow(i + 2);
                dataRow.createCell(1).setCellValue(productionDayMap.get("project_name") != null ? productionDayMap.get("project_name") + "" : "");
                dataRow.createCell(2).setCellValue(productionDayMap.get("product") != null ? productionDayMap.get("product") + "" : "");
                dataRow.createCell(3); //客户需求数量
                dataRow.createCell(4); //客户需求达成率
                dataRow.createCell(5); //目标总产出
                dataRow.createCell(6); //目标达成率
                dataRow.createCell(7).setCellValue(productionDayMap.get("mold") != null ? productionDayMap.get("mold") + "" : ""); //模具
                dataRow.createCell(8).setCellValue(productionDayMap.get("JHHDCHANCHU") != null ? productionDayMap.get("JHHDCHANCHU") + "" : ""); //计划累计产出（颗）
                dataRow.createCell(9).setCellValue(productionDayMap.get("after_output_qty") != null ? productionDayMap.get("after_output_qty") + "" : ""); //实际累计转出（颗）
                dataRow.createCell(10).setCellValue(productionDayMap.get("actual_rate") != null ? productionDayMap.get("actual_rate") + "" : ""); //实际转出达成率
                dataRow.createCell(11).setCellValue(productionDayMap.get("estimate_balance") != null ? productionDayMap.get("estimate_balance") + "" : ""); //WLG预计结存（颗）
                dataRow.createCell(12).setCellValue(productionDayMap.get("estimate_rate") != null ? productionDayMap.get("estimate_rate") + "" : ""); //预计产出达成率
                dataRow.createCell(13); //计划投入模数（模）
                dataRow.createCell(14); //实际投入模数（模）
                dataRow.createCell(15); //实际转出模数（模）
                dataRow.createCell(16); //计划产出（颗)
                dataRow.createCell(17); //实际产出（颗）
                dataRow.createCell(18); //实际产出达成率%
            }
            //填充客户需求数据
            if(customerRequirementDataList != null && customerRequirementDataList.size() > 0)
            {
                for(int i=0; i<productionDataSize; i++)
                {
                    Map<String, Object> productionDayMap = productionDataList.get(i);
                    String projectName = productionDayMap.get("project_name") != null ? productionDayMap.get("project_name") + "" : "";
                    for(int j=0; j<customerRequirementDataList.size(); j++)
                    {
                        Map<String, Object> customerRequirementDataMap = customerRequirementDataList.get(j);
                        if(projectName.equals(customerRequirementDataMap.get("project_name")))
                        {
                            XSSFRow dataRow = sheet.getRow(i + 2);
                            dataRow.getCell(3).setCellValue(customerRequirementDataMap.get("qty") != null ? customerRequirementDataMap.get("qty") + "" : ""); //客户需求数量
                            dataRow.getCell(4).setCellValue(customerRequirementDataMap.get("qty_rate") != null ? customerRequirementDataMap.get("qty_rate") + "" : ""); //客户需求达成率
                            dataRow.getCell(5).setCellValue(customerRequirementDataMap.get("target_yield") != null ? customerRequirementDataMap.get("target_yield") + "" : ""); //目标总产出
                            dataRow.getCell(6).setCellValue(customerRequirementDataMap.get("target_yield_rate") != null ? customerRequirementDataMap.get("target_yield_rate") + "" : ""); //目标达成率
                            break;
                        }
                    }
                }
            }
            //填充当日数据
            if(productionCurrentDayDataList != null && productionCurrentDayDataList.size() > 0)
            {
                for(int i=0; i<productionDataSize; i++)
                {
                    Map<String, Object> productionDayMap = productionDataList.get(i);
                    String projectName = productionDayMap.get("project_name") != null ? productionDayMap.get("project_name") + "" : "";
                    String mold = productionDayMap.get("mold") != null ? productionDayMap.get("mold") + "" : "";
                    for(int j=0; j<productionCurrentDayDataList.size(); j++)
                    {
                        Map<String, Object> productionCurrentDayDataMap = productionCurrentDayDataList.get(j);
                        if(projectName.equals(productionCurrentDayDataMap.get("project_name")) && mold.equals(productionCurrentDayDataMap.get("mold")))
                        {
                            XSSFRow dataRow = sheet.getRow(i + 2);
                            dataRow.getCell(13).setCellValue(productionCurrentDayDataMap.get("JHTOURU") != null ? productionCurrentDayDataMap.get("JHTOURU") + "" : ""); //计划投入模数（模）
                            dataRow.getCell(14).setCellValue(productionCurrentDayDataMap.get("mold_press_input_qty") != null ? productionCurrentDayDataMap.get("mold_press_input_qty") + "" : ""); //实际投入模数（模）
                            dataRow.getCell(15).setCellValue(productionCurrentDayDataMap.get("mold_press_output_qty") != null ? productionCurrentDayDataMap.get("mold_press_output_qty") + "" : ""); //实际转出模数（模）
                            dataRow.getCell(16).setCellValue(productionCurrentDayDataMap.get("JHHDCHANCHU") != null ? productionCurrentDayDataMap.get("JHHDCHANCHU") + "" : ""); //计划产出（颗)
                            dataRow.getCell(17).setCellValue(productionCurrentDayDataMap.get("after_output_qty") != null ? productionCurrentDayDataMap.get("after_output_qty") + "" : ""); //实际产出（颗）
                            dataRow.getCell(18).setCellValue(productionCurrentDayDataMap.get("output_rate") != null ? productionCurrentDayDataMap.get("output_rate") + "" : ""); //实际产出达成率%
                            break;
                        }
                    }
                }
            }
            //合并单元格
            int startRow = 2;
            for(int k=0; k<productionDataSize; k++)
            {
                Map<String, Object> productionDayMap = productionDataList.get(k);
                String mold = productionDayMap.get("mold") != null ? productionDayMap.get("mold") + "" : "";
                if("汇总".equals(mold))
                {
                    //合并单元格
                    for(int l=1; l<=6; l++)
                    {
                        CellRangeAddress cellRegion = new CellRangeAddress(startRow, k+2, l, l);
                        sheet.addMergedRegion(cellRegion);
                    }
                    startRow = k+3;
                }
            }
            //合并单元格
            CellRangeAddress region = new CellRangeAddress(productionDataSize+1, productionDataSize+1, 1, 2);
            sheet.addMergedRegion(region);

            //添加单元格样式
            XSSFCellStyle xssfTitleCellStyle = xssfWorkbook.createCellStyle();
            xssfTitleCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            xssfTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFFont xssfTitleFont = xssfWorkbook.createFont();
            xssfTitleFont.setFontName("微软雅黑");
            xssfTitleFont.setBold(true);
            xssfTitleFont.setFontHeightInPoints((short) 11);
            xssfTitleCellStyle.setFont(xssfTitleFont);

            XSSFCellStyle xssfMoldCellStyle = xssfWorkbook.createCellStyle();
            xssfMoldCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            xssfMoldCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            xssfMoldCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            xssfMoldCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            xssfMoldCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            xssfMoldCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFFont xssfMoldFont = xssfWorkbook.createFont();
            xssfMoldFont.setFontName("微软雅黑");
            xssfMoldFont.setFontHeightInPoints((short) 10);
            xssfMoldCellStyle.setFont(xssfMoldFont);

            XSSFCellStyle xssfContentCellStyle = xssfWorkbook.createCellStyle();
            xssfContentCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            xssfContentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xssfContentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

            XSSFFont xssfContentFont = xssfWorkbook.createFont();
            xssfContentFont.setFontName("微软雅黑");
            xssfContentFont.setFontHeightInPoints((short) 10);
            xssfContentCellStyle.setFont(xssfContentFont);

            for(int j=0; j<productionDataSize; j++)
            {
                XSSFRow dataRow = sheet.getRow(j+2);
                for(int k=1; k<dataRow.getLastCellNum(); k++)
                {
                    XSSFCell cell = dataRow.getCell(k);
                    if(k>=1 && k<=2)
                    {
                        cell.setCellStyle(xssfTitleCellStyle);
                    }
                    else if(k==7)
                    {
                        cell.setCellStyle(xssfMoldCellStyle);
                    }
                    else {
                        cell.setCellStyle(xssfContentCellStyle);
                        if(StringUtils.isNotEmpty(cell.getStringCellValue()) && !cell.getStringCellValue().contains("%")) {
                            cell.setCellValue(Double.parseDouble(cell.getStringCellValue()));
                        }
                    }
                }
            }

            //修改模板内容导出
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            xssfWorkbook.write(out);
            out.close();
        } catch (IOException e) {
            log.error("创建生产报表Excel异常", e);
            return;
        }

        //4 Excel转图片
        Workbook spireXlsWorkbook = new Workbook();
        spireXlsWorkbook.loadFromFile(tempDir + "/" + fileName);
        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
        // Excel转为图片
        worksheet.saveToImage(tempDir + "/" + imageFileName);

        //5 推送图片到群
        DingTalkMessageHistory dingTalkMessageHistory = new DingTalkMessageHistory();
        dingTalkMessageHistory.setProductionDate(currentTime.toLocalDate());

        //获取token
        OapiGettokenResponse oapiGettokenResponse = dingTalkApi.getAccessToken();
        String accessToken = oapiGettokenResponse.getAccessToken();

        //上传图片
        String mediaId = dingTalkApi.uploadMedia(accessToken, "image", tempDir + "/" + imageFileName);
        if(StringUtils.isEmpty(mediaId))
        {
            log.error("上传图片到钉钉异常" + tempDir + "/" + imageFileName);
            return;
        }

        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("WLG项目汇总产出数据（" + currentDate + "）");
        markdownGroupMessage.addContent("![WLG项目汇总产出数据](" + mediaId + ")");

        for(Map<String, String> robotMap : robotList) {
            String robotId = robotMap.get("ROBOT_ID");
            String robotUrl = robotMap.get("ROBOT_URL");

            Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, "WLG项目汇总产出数据", markdownGroupMessage.toString());
            String result = resultMap.get("result");
            String message = resultMap.get("message");
            //5 保存推送历史
            if (!StringUtils.isEmpty(message) && message.length() > 1024) {
                message = message.substring(1024);
            }
            dingTalkMessageHistory.setRobotId(robotId);
            dingTalkMessageHistory.setResult(result);
            dingTalkMessageHistory.setMessage(message);

            dingTalkNotificationMapper.insert(dingTalkMessageHistory);
        }
    }

    @Override
    public void sendGPProductionDayDataImageNotification(String groupType) throws ApiException {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime currentTime = nowTime.minusDays(1);
        LocalDateTime shiftStart = nowTime.toLocalDate().atTime(3, 0, 0); //夜班班次开始时间，取时间为3:00,3:30的数据
        LocalDateTime shiftEnd = nowTime.toLocalDate().atTime(3, 30, 0);//夜班班次结束时间
        //获取当月一号
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);

        int month = currentTime.getMonthValue();
        int monthDay = currentTime.getDayOfMonth();


        String currentDate = currentTime.format(DateTimeFormatter.ofPattern(CURRENT_DATE_FORMAT));
        //1 获取数据
        //获取目标交货数据
        List<Map<String, Object>> targetDeliveryDataList = productionReportService.findTargetDeliveryDataByDate(monthStart.toLocalDate(),
                currentTime.toLocalDate());
        if(targetDeliveryDataList == null || targetDeliveryDataList.size() == 0)
        {
            log.info("没有需要推送到钉钉群的数据");
            return;
        }
        //获取生产汇总数据
        List<Map<String, Object>> productionSummaryDataList = productionReportService.findProductionSummaryDataByDate(monthStart.toLocalDate(), currentTime.toLocalDate());
        //获取项目料号映射关系
        Map<String, String> projectNameItemNumberMap = productionReportService.findProjectNameItemNumberMap(monthStart.toLocalDate(),
                currentTime.toLocalDate());
        //获取需要查询的物料号
        List<String> itemNumberList = productionReportService.findItemNumberListByDate(monthStart.toLocalDate(),
                currentTime.toLocalDate());
        //获取实际出货数据
        List<Map<String, Object>> actualDeliveryDataList = null;
        if(itemNumberList != null && itemNumberList.size() > 0)
        {
            actualDeliveryDataList = productionReportService.findDeliveryDataByDate(monthStart.toLocalDate(), currentTime.toLocalDate(), itemNumberList);
        }

        //项目列表
        List<String> projectNameList = new ArrayList<>();
        for (String key : projectNameItemNumberMap.keySet()) {
            projectNameList.add(key);
        }

        //获取商务项目和内部项目映射
        Map<String, String> projectMap = null;
        if(projectNameList != null && projectNameList.size() > 0)
        {
            projectMap = productionReportService.findProjectMapList(projectNameList);
        }
        //获取内部项目
        List<String> internalProjectList = new ArrayList<>();
        if(projectMap != null && projectMap.size() > 0)
        {
            for (Map.Entry<String, String> entry : projectMap.entrySet()) {
                String internalProject = entry.getKey();
                String businessProject = entry.getValue();

                internalProjectList.add(internalProject);
            }
        }


        //获取结存数据
        List<Map<String, Object>> warehouseBalanceDataList = new ArrayList<>();
        if(projectNameList != null && projectNameList.size() > 0)
        {
            List<Map<String, Object>> internalProjectWarehouseBalanceDataList = productionReportService.findWarehouseBalanceDataByDate(shiftStart, shiftEnd, internalProjectList);
            //按商务项目组装数据
            if(internalProjectWarehouseBalanceDataList != null && internalProjectWarehouseBalanceDataList.size()>0)
            {
                for(String businessProject : projectNameList) {
                    Map<String, Object> warehouseBalanceDataMap = new HashMap<>();
                    warehouseBalanceDataMap.put("project_name", businessProject);

                    int sumQty = 0;
                    for (Map<String, Object> internalProjectWarehouseBalanceData : internalProjectWarehouseBalanceDataList) {
                        String tempInternalProject = internalProjectWarehouseBalanceData.get("project_name")+"";
                        String tempBusinessProject = projectMap.get(tempInternalProject);

                        if(businessProject.equals(tempBusinessProject)) {
                            int tempSumQty = Integer.valueOf(internalProjectWarehouseBalanceData.get("sumQty") + "");
                            sumQty += tempSumQty;
                        }
                    }
                    warehouseBalanceDataMap.put("sumQty", sumQty);
                    warehouseBalanceDataList.add(warehouseBalanceDataMap);
                }
            }
        }

        //2 获取机器人
        List<Map<String, String>> robotList = dingTalkNotificationMapper.findRobotListByType(groupType);
        if(robotList == null || robotList.size() == 0)
        {
            log.info("没有维护机器人");
            return;
        }

        //3 导出成Excel
        //excel模板路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/G+P产出报表模板.xlsx");

        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "G+P生产报表-" + currentTimeMillis + ".xlsx";
        String imageFileName = "G+P生产报表-" + currentTimeMillis + ".png";

        int targetDeliverySize = targetDeliveryDataList.size();
        //读取excel模板
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);

            //读取了模板内所有sheet内容
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //替换表头中的日期
            XSSFRow titleRow = sheet.getRow(0);
            for(int i=2; i<=13; i++)
            {
                XSSFCell titleCell = titleRow.getCell(i);
                String cellValue = titleCell.getStringCellValue();
                String resultCellValue = cellValue.replace("当月", month+"月").replace("当日", currentDate);
                titleCell.setCellValue(resultCellValue);
            }


            //如果这行没有了，整个公式都不会有自动计算的效果的
//            sheet.setForceFormulaRecalculation(true);
            for(int i=0; i<targetDeliverySize; i++)
            {
                Map<String, Object> targetDeliveryMap = targetDeliveryDataList.get(i);
                XSSFRow dataRow = sheet.createRow(i + 2);
                dataRow.createCell(1).setCellValue(targetDeliveryMap.get("project_name") != null ? targetDeliveryMap.get("project_name") + "" : "");
                dataRow.createCell(2).setCellValue(targetDeliveryMap.get("delivery_qty") != null ? targetDeliveryMap.get("delivery_qty") + "" : "");//目标交货数量
                dataRow.createCell(3); //实际交货数量
                dataRow.createCell(4); //实际交货率
                dataRow.createCell(5); //目标产出数量
                dataRow.createCell(6); //实际产出数量
                dataRow.createCell(7); //实际出货率
                dataRow.createCell(8); //实际结存数量
                dataRow.createCell(9).setCellValue(targetDeliveryMap.get("day_delivery_qty") != null ? targetDeliveryMap.get("day_delivery_qty") + "" : ""); //当日目标交货数量
                dataRow.createCell(10); //当日实际交货数量
                dataRow.createCell(11); //当日实际交货率
                dataRow.createCell(12); //当日目标产出数量
                dataRow.createCell(13); //当日实际产出数量
                dataRow.createCell(14); //当日实际出货率
            }
            //填充实际交货数据
            if(actualDeliveryDataList != null && actualDeliveryDataList.size() > 0)
            {
                for(int i=0; i<targetDeliverySize; i++)
                {
                    Map<String, Object> productionDayMap = targetDeliveryDataList.get(i);
                    String projectName = productionDayMap.get("project_name") != null ? productionDayMap.get("project_name") + "" : "";
                    String itemNumber = "";
                    if("项目汇总".equals(projectName))
                    {
                        itemNumber = "项目汇总";
                    }else
                    {
                        itemNumber = projectNameItemNumberMap.get(projectName);
                    }
                    if(itemNumber == null)
                    {
                        continue;
                    }
                    for(int j=0; j<actualDeliveryDataList.size(); j++)
                    {
                        Map<String, Object> actualDeliveryDataMap = actualDeliveryDataList.get(j);
                        if(itemNumber.equals(actualDeliveryDataMap.get("matnr")))
                        {
                            XSSFRow dataRow = sheet.getRow(i + 2);
                            String targetDeliveryQtyStr = dataRow.getCell(2).getStringCellValue();
                            String targetDayDeliveryQtyStr = dataRow.getCell(9).getStringCellValue();

                            String actualDeliveryQtyStr = actualDeliveryDataMap.get("fkimg") != null ? actualDeliveryDataMap.get("fkimg") + "" : "";
                            String actualDayDeliveryQtyStr = actualDeliveryDataMap.get("day_fkimg") != null ? actualDeliveryDataMap.get("day_fkimg") + "" : "";

                            if(StringUtils.isNotEmpty(targetDeliveryQtyStr))
                            {
                                BigDecimal targetDeliveryQty = new BigDecimal(targetDeliveryQtyStr);
                                BigDecimal targetDayDeliveryQty = new BigDecimal(targetDayDeliveryQtyStr);
                                if(StringUtils.isNotEmpty(actualDeliveryQtyStr))
                                {
                                    BigDecimal actualDeliveryQty = new BigDecimal(actualDeliveryQtyStr);
                                    BigDecimal actualDayDeliveryQty = new BigDecimal(actualDayDeliveryQtyStr);
                                    String actualDeliveryRate = "0.00%";
                                    String actualDayDeliveryRate = "0.00%";
                                    if(actualDeliveryQty.compareTo(BigDecimal.ZERO) > 0)
                                    {
                                        actualDeliveryRate = (actualDeliveryQty.divide(targetDeliveryQty, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)) + "%";
                                        actualDayDeliveryRate = (actualDayDeliveryQty.divide(targetDayDeliveryQty, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)) + "%";
                                    }
                                    dataRow.getCell(4).setCellValue(actualDeliveryRate); //实际交货率
                                    dataRow.getCell(11).setCellValue(actualDayDeliveryRate); //当日实际交货率
                                }
                            }

                            dataRow.getCell(3).setCellValue(actualDeliveryQtyStr); //实际交货数量
                            dataRow.getCell(10).setCellValue(actualDayDeliveryQtyStr);//当日实际交货数量
                            break;
                        }
                    }
                }
            }

            //填充生产汇总数据
            if(productionSummaryDataList != null && productionSummaryDataList.size() > 0)
            {
                for(int i=0; i<targetDeliverySize; i++)
                {
                    Map<String, Object> productionDayMap = targetDeliveryDataList.get(i);
                    String projectName = productionDayMap.get("project_name") != null ? productionDayMap.get("project_name") + "" : "";
                    for(int j=0; j<productionSummaryDataList.size(); j++)
                    {
                        Map<String, Object> productionSummaryDataMap = productionSummaryDataList.get(j);
                        if(projectName.equals(productionSummaryDataMap.get("project_name")))
                        {
                            XSSFRow dataRow = sheet.getRow(i + 2);
                            dataRow.getCell(5).setCellValue(productionSummaryDataMap.get("target_qty") != null ? productionSummaryDataMap.get("target_qty") + "" : ""); //目标生产数量
                            dataRow.getCell(6).setCellValue(productionSummaryDataMap.get("actual_qty") != null ? productionSummaryDataMap.get("actual_qty") + "" : ""); //实际生产数量
                            dataRow.getCell(7).setCellValue(productionSummaryDataMap.get("actual_rate") != null ? productionSummaryDataMap.get("actual_rate") + "" : ""); //实际出货数量
                            dataRow.getCell(12).setCellValue(productionSummaryDataMap.get("day_target_qty") != null ? productionSummaryDataMap.get("day_target_qty") + "" : ""); //当日目标生产数量
                            dataRow.getCell(13).setCellValue(productionSummaryDataMap.get("day_actual_qty") != null ? productionSummaryDataMap.get("day_actual_qty") + "" : ""); //当日实际生产数量
                            dataRow.getCell(14).setCellValue(productionSummaryDataMap.get("day_actual_rate") != null ? productionSummaryDataMap.get("day_actual_rate") + "" : ""); //当日实际出货数量
                            break;
                        }
                    }
                }
            }

            //填充结存数据
            if(warehouseBalanceDataList != null && warehouseBalanceDataList.size() > 0)
            {
                for(int i=0; i<targetDeliverySize; i++)
                {
                    Map<String, Object> targetDeliveryMap = targetDeliveryDataList.get(i);
                    String projectName = targetDeliveryMap.get("project_name") != null ? targetDeliveryMap.get("project_name") + "" : "";
                    for(int j=0; j<warehouseBalanceDataList.size(); j++)
                    {
                        Map<String, Object> warehouseBalanceDataMap = warehouseBalanceDataList.get(j);
                        if(projectName.equals(warehouseBalanceDataMap.get("project_name")))
                        {
                            XSSFRow dataRow = sheet.getRow(i + 2);
                            dataRow.getCell(8).setCellValue(warehouseBalanceDataMap.get("sumQty") != null ? warehouseBalanceDataMap.get("sumQty") + "" : ""); //结存数据
                            break;
                        }
                    }
                }
            }


            //添加单元格样式
            XSSFCellStyle xssfTitleCellStyle = xssfWorkbook.createCellStyle();
            xssfTitleCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            xssfTitleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            xssfTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFFont xssfTitleFont = xssfWorkbook.createFont();
            xssfTitleFont.setFontName("微软雅黑");
            xssfTitleFont.setBold(true);
            xssfTitleFont.setFontHeightInPoints((short) 11);
            xssfTitleCellStyle.setFont(xssfTitleFont);

            XSSFCellStyle xssfContentCellStyle = xssfWorkbook.createCellStyle();
            xssfContentCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            xssfContentCellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            xssfContentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xssfContentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

            XSSFFont xssfContentFont = xssfWorkbook.createFont();
            xssfContentFont.setFontName("微软雅黑");
            xssfContentFont.setFontHeightInPoints((short) 10);
            xssfContentCellStyle.setFont(xssfContentFont);

            for(int j=0; j<targetDeliverySize; j++)
            {
                XSSFRow dataRow = sheet.getRow(j+2);
                for(int k=1; k<dataRow.getLastCellNum(); k++)
                {
                    XSSFCell cell = dataRow.getCell(k);
                    if(k==1)
                    {
                        cell.setCellStyle(xssfTitleCellStyle);
                    }
                    else {
                        cell.setCellStyle(xssfContentCellStyle);
                        if(StringUtils.isNotEmpty(cell.getStringCellValue()) && !cell.getStringCellValue().contains("%")) {
                            cell.setCellValue(Double.parseDouble(cell.getStringCellValue()));
                        }
                    }
                }
            }

            //修改模板内容导出
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            xssfWorkbook.write(out);
            out.close();
        } catch (IOException e) {
            log.error("创建生产报表Excel异常", e);
            return;
        }

        //4 Excel转图片
        Workbook spireXlsWorkbook = new Workbook();
        spireXlsWorkbook.loadFromFile(tempDir + "/" + fileName);
        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
        // Excel转为图片
        worksheet.saveToImage(tempDir + "/" + imageFileName);

        //5 推送图片到群
        DingTalkMessageHistory dingTalkMessageHistory = new DingTalkMessageHistory();
        dingTalkMessageHistory.setProductionDate(currentTime.toLocalDate());

        //获取token
        OapiGettokenResponse oapiGettokenResponse = dingTalkApi.getAccessToken();
        String accessToken = oapiGettokenResponse.getAccessToken();

        //上传图片
        String mediaId = dingTalkApi.uploadMedia(accessToken, "image", tempDir + "/" + imageFileName);
        if(StringUtils.isEmpty(mediaId))
        {
            log.error("上传图片到钉钉异常" + tempDir + "/" + imageFileName);
            return;
        }

        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("G+P项目汇总产出数据（" + currentDate + "）");
        markdownGroupMessage.addContent("![G+P项目汇总产出数据](" + mediaId + ")");

        for(Map<String, String> robotMap : robotList) {
            String robotId = robotMap.get("ROBOT_ID");
            String robotUrl = robotMap.get("ROBOT_URL");

            Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, "G+P项目汇总产出数据", markdownGroupMessage.toString());
            String result = resultMap.get("result");
            String message = resultMap.get("message");
            //5 保存推送历史
            if (!StringUtils.isEmpty(message) && message.length() > 1024) {
                message = message.substring(1024);
            }
            dingTalkMessageHistory.setRobotId(robotId);
            dingTalkMessageHistory.setResult(result);
            dingTalkMessageHistory.setMessage(message);

            dingTalkNotificationMapper.insert(dingTalkMessageHistory);
        }
    }
}
