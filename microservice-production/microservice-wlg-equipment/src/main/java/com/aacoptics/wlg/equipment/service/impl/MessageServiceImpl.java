package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.MessageTypeConstants;
import com.aacoptics.wlg.equipment.constant.NotificationStatusConstants;
import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.entity.vo.RepairOrderVO;
import com.aacoptics.wlg.equipment.entity.vo.SectionSummaryOrderVO;
import com.aacoptics.wlg.equipment.provider.FeishuApi;
import com.aacoptics.wlg.equipment.provider.NotificationProvider;
import com.aacoptics.wlg.equipment.service.*;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private static final String RECEIVE_TYPE = "user_id";

    @Resource
    private NotificationProvider notificationProvider;

    @Resource
    private InspectionOrderService inspectionOrderService;

    @Resource
    private MaintenanceOrderService maintenanceOrderService;

    @Resource
    private RepairOrderService repairOrderService;

    @Resource
    private MessageHistoryService messageHistoryService;

    @Resource
    private EquipmentService equipmentService;

    @Resource
    private ReportService reportService;

    @Resource
    private FeishuService feishuService;

    @Resource
    private FeishuApi feishuApi;


    @Override
    public boolean sendInspectionExceptionMessage() {
        List<InspectionOrder> inspectionOrderList = inspectionOrderService.findInspectionExceptionOrder();
        if (inspectionOrderList == null || inspectionOrderList.size() == 0) {
            log.warn("没有需要推送的点检异常工单");
            return true;
        }
        for (int i = 0; i < inspectionOrderList.size(); i++) {
            InspectionOrder inspectionOrder = inspectionOrderList.get(i);
            inspectionOrder = inspectionOrderService.get(inspectionOrder.getId());
            String dutyPersonId = inspectionOrder.getDutyPersonId();
            String mchCode = inspectionOrder.getMchCode();
            String orderNumber = inspectionOrder.getOrderNumber();
            //获取设备
            Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
            String equipNumber = equipment.getEquipNumber();
            String mchName = equipment.getMchName();

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG点检设备异常通知**  \n");
            contentStringBuffer.append("工单号：" + orderNumber + "  \n");
            contentStringBuffer.append("资产编码：" + mchCode + "  \n");
            contentStringBuffer.append("设备编号：" + equipNumber + "  \n");
            contentStringBuffer.append("设备名称：" + mchName + "  \n");
            contentStringBuffer.append("存在点检项异常：  \n");

            List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();


            for (int j = 0; j < inspectionOrderItemList.size(); j++) {
                InspectionOrderItem inspectionOrderItem = inspectionOrderItemList.get(j);
                if (inspectionOrderItem.getIsException() != null && inspectionOrderItem.getIsException() == 1) {
                    BigDecimal minValue = inspectionOrderItem.getMinValue();
                    BigDecimal maxValue = inspectionOrderItem.getMaxValue();
                    BigDecimal actualValue = inspectionOrderItem.getActualValue();
                    contentStringBuffer.append(inspectionOrderItem.getCheckItem() +
                            "：参数范围值为" + minValue.stripTrailingZeros().toPlainString()
                            + "到" + maxValue.stripTrailingZeros().toPlainString() +
                            "，实际值为" + (actualValue != null ? actualValue.stripTrailingZeros().toPlainString() : "") + "  \n");
                }
            }
            contentStringBuffer.append("请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if (result.isSuccess()) {
                MessageHistory messageHistory = new MessageHistory();
                messageHistory.setMchCode(mchCode);
                messageHistory.setOrderNumber(orderNumber);
                messageHistory.setReceiveId(dutyPersonId);
                messageHistory.setReceiveType(RECEIVE_TYPE);
                messageHistory.setType(MessageTypeConstants.INSPECTION_EXCEPTION);
                messageHistory.setMessage(content);

                messageHistoryService.add(messageHistory);

                //更新异常推送通知状态
                inspectionOrder.setExceptionNotification(NotificationStatusConstants.YES);
                inspectionOrderService.updateById(inspectionOrder);

            } else {
                log.error("推送点检异常消息到飞书失败，工单号：" + orderNumber + "，" + result.getMsg());
            }
        }
        return true;
    }


    @Override
    public boolean sendMaintenanceExceptionMessage() {
        List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.findMaintenanceExceptionOrder();
        if (maintenanceOrderList == null || maintenanceOrderList.size() == 0) {
            log.warn("没有需要推送的保养异常工单");
            return true;
        }
        for (int i = 0; i < maintenanceOrderList.size(); i++) {
            MaintenanceOrder maintenanceOrder = maintenanceOrderList.get(i);
            maintenanceOrder = maintenanceOrderService.get(maintenanceOrder.getId());
            String dutyPersonId = maintenanceOrder.getDutyPersonId();
            String mchCode = maintenanceOrder.getMchCode();
            String orderNumber = maintenanceOrder.getOrderNumber();

            //获取设备
            Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
            String equipNumber = equipment.getEquipNumber();
            String mchName = equipment.getMchName();

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG保养设备异常通知**  \n");
            contentStringBuffer.append("工单号：" + orderNumber + "  \n");
            contentStringBuffer.append("资产编码：" + mchCode + "  \n");
            contentStringBuffer.append("设备编号：" + equipNumber + "  \n");
            contentStringBuffer.append("设备名称：" + mchName + "  \n");
            contentStringBuffer.append("存在保养项异常：  \n");

            List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrder.getMaintenanceOrderItemList();

            for (int j = 0; j < maintenanceOrderItemList.size(); j++) {
                MaintenanceOrderItem maintenanceOrderItem = maintenanceOrderItemList.get(j);
                if (maintenanceOrderItem.getIsException() != null && maintenanceOrderItem.getIsException() == 1) {
                    BigDecimal minValue = maintenanceOrderItem.getMinValue();
                    BigDecimal maxValue = maintenanceOrderItem.getMaxValue();
                    BigDecimal actualValue = maintenanceOrderItem.getActualValue();
                    contentStringBuffer.append(maintenanceOrderItem.getMaintenanceItem() +
                            "：参数范围值为" + minValue.stripTrailingZeros().toPlainString()
                            + "到" + maxValue.stripTrailingZeros().toPlainString() +
                            "，实际值为" + (actualValue != null ? actualValue.stripTrailingZeros().toPlainString() : "") + "  \n");
                }
            }
            contentStringBuffer.append("请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if (result.isSuccess()) {
                MessageHistory messageHistory = new MessageHistory();
                messageHistory.setMchCode(mchCode);
                messageHistory.setOrderNumber(orderNumber);
                messageHistory.setReceiveId(dutyPersonId);
                messageHistory.setReceiveType(RECEIVE_TYPE);
                messageHistory.setType(MessageTypeConstants.MAINTENANCE_EXCEPTION);
                messageHistory.setMessage(content);

                messageHistoryService.add(messageHistory);

                //更新异常推送通知状态
                maintenanceOrder.setExceptionNotification(NotificationStatusConstants.YES);
                maintenanceOrderService.updateById(maintenanceOrder);

            } else {
                log.error("推送保养异常消息到飞书失败，工单号：" + orderNumber + "，" + result.getMsg());
            }
        }
        return true;
    }

    @Override
    public boolean sendInspectionTimeoutMessage() {
        List<String> dutyPersonIdList = inspectionOrderService.findInspectionTimeoutOrderDutyPersonIdList();
        if (dutyPersonIdList == null || dutyPersonIdList.size() == 0) {
            log.warn("没有需要推送的点检超时工单");
            return true;
        }

        for (int i = 0; i < dutyPersonIdList.size(); i++) {
            String dutyPersonId = dutyPersonIdList.get(i);
            List<InspectionOrder> inspectionOrderList = inspectionOrderService.findInspectionTimeoutOrderByDutyPersonId(dutyPersonId);
            if (inspectionOrderList == null || inspectionOrderList.size() == 0) {
                continue;
            }

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG设备点检超时通知**  \n");
            for (int j = 0; j < inspectionOrderList.size(); j++) {
                InspectionOrder inspectionOrder = inspectionOrderList.get(j);
                String mchCode = inspectionOrder.getMchCode();
                String orderNumber = inspectionOrder.getOrderNumber();
                //获取设备
                Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
                String equipNumber = equipment.getEquipNumber();
                String mchName = equipment.getMchName();

                contentStringBuffer.append("工单号：" + orderNumber + "，资产编码：" + mchCode + "，设备编号：" + equipNumber + "  \n");
            }

            contentStringBuffer.append("未在班次内按时进行点检，请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if (result.isSuccess()) {
                for (InspectionOrder inspectionOrder : inspectionOrderList) {
                    String mchCode = inspectionOrder.getMchCode();
                    String orderNumber = inspectionOrder.getOrderNumber();

                    MessageHistory messageHistory = new MessageHistory();
                    messageHistory.setMchCode(mchCode);
                    messageHistory.setOrderNumber(orderNumber);
                    messageHistory.setReceiveId(dutyPersonId);
                    messageHistory.setReceiveType(RECEIVE_TYPE);
                    messageHistory.setType(MessageTypeConstants.INSPECTION_TIME_OUT);
                    messageHistory.setMessage(content);

                    messageHistoryService.add(messageHistory);

                    //更新超时推送通知状态
                    inspectionOrder.setTimeoutNotification(NotificationStatusConstants.YES);
                    inspectionOrderService.updateById(inspectionOrder);
                }

            } else {
                log.error("推送点检超时消息到飞书失败，责任人：" + dutyPersonId + "，" + result.getMsg());
            }
        }
        return true;
    }

    @Override
    public boolean sendMaintenanceTimeoutMessage() {
        List<String> dutyPersonIdList = maintenanceOrderService.findMaintenanceTimeoutOrderDutyPersonIdList();
        if (dutyPersonIdList == null || dutyPersonIdList.size() == 0) {
            log.warn("没有需要推送的保养超时工单");
            return true;
        }
        for (int i = 0; i < dutyPersonIdList.size(); i++) {
            String dutyPersonId = dutyPersonIdList.get(i);
            List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.findMaintenanceTimeoutOrderByDutyPersonId(dutyPersonId);
            if (maintenanceOrderList == null || maintenanceOrderList.size() == 0) {
                continue;
            }

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG设备保养超时通知**  \n");

            for (MaintenanceOrder maintenanceOrder : maintenanceOrderList) {
                String mchCode = maintenanceOrder.getMchCode();
                String orderNumber = maintenanceOrder.getOrderNumber();

                //获取设备
                Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
                String equipNumber = equipment.getEquipNumber();
                String mchName = equipment.getMchName();

                contentStringBuffer.append("工单号：" + orderNumber + "，资产编码：" + mchCode + "，设备编号：" + equipNumber + "  \n");
            }

            contentStringBuffer.append("未按时保养，请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if (result.isSuccess()) {
                for (MaintenanceOrder maintenanceOrder : maintenanceOrderList) {
                    String mchCode = maintenanceOrder.getMchCode();
                    String orderNumber = maintenanceOrder.getOrderNumber();

                    MessageHistory messageHistory = new MessageHistory();
                    messageHistory.setMchCode(mchCode);
                    messageHistory.setOrderNumber(orderNumber);
                    messageHistory.setReceiveId(dutyPersonId);
                    messageHistory.setReceiveType(RECEIVE_TYPE);
                    messageHistory.setType(MessageTypeConstants.MAINTENANCE_TIME_OUT);
                    messageHistory.setMessage(content);

                    messageHistoryService.add(messageHistory);

                    //更新超时推送通知状态
                    maintenanceOrder.setTimeoutNotification(NotificationStatusConstants.YES);
                    maintenanceOrderService.updateById(maintenanceOrder);
                }

            } else {
                log.error("推送保养超时消息到飞书失败，责任人：" + dutyPersonId + "，" + result.getMsg());
            }
        }
        return true;
    }

    @Override
    public boolean sendRepairMessage(RepairOrder repairOrder) {
        if (repairOrder == null) {
            log.warn("维修工单不能为空");
            return true;
        }
        String orderNumber = repairOrder.getOrderNumber();
        String mchCode = repairOrder.getMchCode();
        String dutyPersonId = repairOrder.getDutyPersonId();

        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);

        StringBuffer contentStringBuffer = new StringBuffer();
        contentStringBuffer.append("**WLG设备维修工单通知**  \n");
        contentStringBuffer.append("工单号：" + orderNumber + "  \n");
        contentStringBuffer.append("资产编码：" + repairOrder.getMchCode() + "  \n");
        contentStringBuffer.append("设备编号：" + equipment.getEquipNumber() + "  \n");
        contentStringBuffer.append("设备名称：" + equipment.getMchName() + "  \n");
        contentStringBuffer.append("故障描述：" + repairOrder.getFaultDesc() + "  \n");
        contentStringBuffer.append("请注意处理！");

        String content = contentStringBuffer.toString();

        FeishuMessage feishuMessage = new FeishuMessage();
        feishuMessage.setSendType(RECEIVE_TYPE);
        feishuMessage.setSendId(dutyPersonId);
        feishuMessage.setContent(content);

        Result result = notificationProvider.sendFeishuNotification(feishuMessage);
        if (result.isSuccess()) {
            MessageHistory messageHistory = new MessageHistory();
            messageHistory.setMchCode(mchCode);
            messageHistory.setOrderNumber(orderNumber);
            messageHistory.setReceiveId(dutyPersonId);
            messageHistory.setReceiveType(RECEIVE_TYPE);
            messageHistory.setType(MessageTypeConstants.REPAIR_ORDER);
            messageHistory.setMessage(content);

            messageHistoryService.add(messageHistory);
        } else {
            log.error("推送维修工单消息到飞书失败，工单号：" + orderNumber + "，" + result.getMsg());
        }
        return true;
    }

    @Override
    public boolean sendEquipmentAllRepairMessage(RepairOrder repairOrder) {
        if (repairOrder == null) {
            log.warn("维修工单不能为空");
            return true;
        }
        String mchCode = repairOrder.getMchCode();
        String dutyPersonId = repairOrder.getDutyPersonId();

        List<RepairOrderVO> repairOrderVOList = repairOrderService.findOrderByMchCode(mchCode);

        Equipment equipment = equipmentService.findEquipmentByMchCode(mchCode);
        String equipDutyManager = equipment.getEquipDutyManager(); //设备负责人经理

        StringBuffer contentStringBuffer = new StringBuffer();
        contentStringBuffer.append("**WLG设备维修工单通知**  \n");
        contentStringBuffer.append("资产编码：" + repairOrder.getMchCode() + ",设备编号：" + equipment.getEquipNumber() + ",设备名称：" + equipment.getMchName() + "  \n");
        for(RepairOrderVO repairOrderVO : repairOrderVOList) {
            String orderNumber = repairOrderVO.getOrderNumber();
            String faultDesc = repairOrderVO.getFaultDesc();
            String faultTime = repairOrderVO.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            contentStringBuffer.append("工单号：" + orderNumber + ",报修时间：" + faultTime + ",故障描述：" + faultDesc + "  \n");
        }
        contentStringBuffer.append("请注意处理！");

        String content = contentStringBuffer.toString();

        FeishuMessage feishuMessage = new FeishuMessage();
        feishuMessage.setSendType(RECEIVE_TYPE);
        feishuMessage.setSendId(dutyPersonId);
        feishuMessage.setContent(content);

        Result result = notificationProvider.sendFeishuNotification(feishuMessage);
        if (result.isSuccess()) {
            MessageHistory messageHistory = new MessageHistory();
            messageHistory.setMchCode(mchCode);
            messageHistory.setOrderNumber(repairOrder.getOrderNumber());
            messageHistory.setReceiveId(dutyPersonId);
            messageHistory.setReceiveType(RECEIVE_TYPE);
            messageHistory.setType(MessageTypeConstants.REPAIR_ORDER);
            messageHistory.setMessage(content);

            messageHistoryService.add(messageHistory);
        } else {
            log.error("推送维修工单消息到飞书失败，工单号：" + repairOrder.getOrderNumber() + "，" + result.getMsg());
        }

        //推送给设备负责人经理
        if(StringUtils.isNotEmpty(equipDutyManager) && !dutyPersonId.equals(equipDutyManager))
        {
            feishuMessage.setSendId(equipDutyManager);
            Result sendResult = notificationProvider.sendFeishuNotification(feishuMessage);
            if (sendResult.isSuccess()) {
                MessageHistory messageHistory = new MessageHistory();
                messageHistory.setMchCode(mchCode);
                messageHistory.setOrderNumber(repairOrder.getOrderNumber());
                messageHistory.setReceiveId(equipDutyManager);
                messageHistory.setReceiveType(RECEIVE_TYPE);
                messageHistory.setType(MessageTypeConstants.REPAIR_ORDER);
                messageHistory.setMessage(content);

                messageHistoryService.add(messageHistory);
            } else {
                log.error("推送维修工单消息到飞书失败，工单号：" + repairOrder.getOrderNumber() + "，" + sendResult.getMsg());
            }
        }

        return true;
    }


    @Override
    public boolean sendSectionOrderCountMessage() {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime monthStart = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.of(LocalDate.from(currentTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);

        int year = currentTime.getYear();
        int month = currentTime.getMonthValue();


        //获取需要推送的数据
        SectionSummaryOrderQueryParam sectionSummaryOrderQueryParam = new SectionSummaryOrderQueryParam();
        sectionSummaryOrderQueryParam.setCreateDateStart(monthStart);
        sectionSummaryOrderQueryParam.setCreateDateEnd(monthEnd);

        List<SectionSummaryOrderVO> sectionSummaryOrderVOList = reportService.findSectionOrderCount(sectionSummaryOrderQueryParam);
        //excel模板路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/sectionOrderCount.xlsx");

        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "sectionOrderCount-" + currentTimeMillis + ".xlsx";
        String imageFileName = "sectionOrderCount-" + currentTimeMillis + ".png";

        //读取excel模板
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);

            //读取了模板内所有sheet内容
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //替换表头中的日期
            XSSFRow titleRow = sheet.getRow(0);

            XSSFCell titleCell = titleRow.getCell(0);
            String cellValue = titleCell.getStringCellValue();
            String resultCellValue = cellValue.replace("当月", year + "年" + month+"月");
            titleCell.setCellValue(resultCellValue);

            if(sectionSummaryOrderVOList != null && sectionSummaryOrderVOList.size() > 0)
            {
                for(int i=0; i<sectionSummaryOrderVOList.size(); i++)
                {
                    SectionSummaryOrderVO sectionSummaryOrderVO = sectionSummaryOrderVOList.get(i);

                    XSSFRow dataRow = sheet.createRow(i + 2);

                    this.createCell(xssfWorkbook, dataRow, 0).setCellValue(sectionSummaryOrderVO.getSectionType());
                    this.createCell(xssfWorkbook, dataRow, 1).setCellValue(sectionSummaryOrderVO.getInspectionOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 2).setCellValue(sectionSummaryOrderVO.getCommittedInspectionOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 3).setCellValue(sectionSummaryOrderVO.getConfirmedInspectionOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 4).setCellValue(sectionSummaryOrderVO.getUnfinishedInspectionOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 5).setCellValue(sectionSummaryOrderVO.getMaintenanceOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 6).setCellValue(sectionSummaryOrderVO.getCommittedMaintenanceOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 7).setCellValue(sectionSummaryOrderVO.getConfirmedMaintenanceOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 8).setCellValue(sectionSummaryOrderVO.getUnfinishedMaintenanceOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 9).setCellValue(sectionSummaryOrderVO.getRepairOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 10).setCellValue(sectionSummaryOrderVO.getCommittedRepairOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 11).setCellValue(sectionSummaryOrderVO.getConfirmedRepairOrderCount());
                    this.createCell(xssfWorkbook, dataRow, 12).setCellValue(sectionSummaryOrderVO.getUnfinishedRepairOrderCount());
                }
            }

            //修改模板内容后导出
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            xssfWorkbook.write(out);
            out.close();

        } catch (IOException e) {
            log.error("创建Excel异常" + e.getLocalizedMessage());
            log.error("创建Excel异常", e);
            return false;
        }
        //4 Excel转图片
        Workbook spireXlsWorkbook = new Workbook();
        spireXlsWorkbook.loadFromFile(tempDir + "/" + fileName);
        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
        // Excel转为图片
        worksheet.saveToImage(tempDir + "/" + imageFileName);

        //图片上传到飞书
        String imageKey = null;
        try {
            imageKey = feishuService.fetchUploadMessageImageKey(tempDir + "/" + imageFileName);
        } catch (IOException e) {
            log.error("图片上传到飞书异常" + e.getLocalizedMessage());
            log.error("图片上传到飞书异常", e);
            return false;
        }
        log.info("上传图片到飞书成功，image_key=" + imageKey);

        //推送到飞书群
//        feishuApi.sendGroupMessage();

        return true;
    }


    private XSSFCell createCell(XSSFWorkbook xssfWorkbook, XSSFRow dataRow, int columnIndex)
    {
        //设置单元格样式
        XSSFCellStyle xssfContentCellStyle = xssfWorkbook.createCellStyle();
        xssfContentCellStyle.setBorderTop(BorderStyle.THIN);
        xssfContentCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfContentCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfContentCellStyle.setBorderRight(BorderStyle.THIN);
        xssfContentCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        xssfContentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFCell dataCell = dataRow.createCell(columnIndex);
        dataCell.setCellStyle(xssfContentCellStyle);

        return dataCell;
    }
}
