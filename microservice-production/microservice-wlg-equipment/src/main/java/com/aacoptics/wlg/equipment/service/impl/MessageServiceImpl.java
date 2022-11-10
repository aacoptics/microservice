package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.MessageTypeConstants;
import com.aacoptics.wlg.equipment.constant.NotificationStatusConstants;
import com.aacoptics.wlg.equipment.entity.po.*;
import com.aacoptics.wlg.equipment.provider.NotificationProvider;
import com.aacoptics.wlg.equipment.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private MessageHistoryService messageHistoryService;

    @Resource
    private EquipmentService equipmentService;


    @Override
    public boolean sendInspectionExceptionMessage() {
        List<InspectionOrder> inspectionOrderList = inspectionOrderService.findInspectionExceptionOrder();
        if (inspectionOrderList == null || inspectionOrderList.size() == 0)
        {
            log.warn("没有需要推送的点检异常工单");
            return true;
        }
        for(int i=0; i<inspectionOrderList.size(); i++)
        {
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
            contentStringBuffer.append("工单号：" +orderNumber + "  \n");
            contentStringBuffer.append("资产编码：" +mchCode + "  \n");
            contentStringBuffer.append("设备编号：" +equipNumber + "  \n");
            contentStringBuffer.append("设备名称：" +mchName + "  \n");
            contentStringBuffer.append("存在点检项异常：  \n");

            List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();

            int index = 1;
            for(int j=0; j<inspectionOrderItemList.size(); j++)
            {
                InspectionOrderItem inspectionOrderItem = inspectionOrderItemList.get(j);
                if(inspectionOrderItem.getIsException() != null && inspectionOrderItem.getIsException() == 1)
                {
                    BigDecimal minValue = inspectionOrderItem.getMinValue();
                    BigDecimal maxValue = inspectionOrderItem.getMaxValue();
                    BigDecimal actualValue = inspectionOrderItem.getActualValue();
                    contentStringBuffer.append(index++ + "." + inspectionOrderItem.getCheckItem() +
                            "：参数范围值为" + minValue.stripTrailingZeros().toPlainString()
                            + "到" + maxValue.stripTrailingZeros().toPlainString() +
                            "，实际值为" + (actualValue != null ? actualValue.stripTrailingZeros().toPlainString() : "")  + "  \n");
                }
            }
            contentStringBuffer.append("请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if(result.isSuccess()){
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

            }
            else{
                log.error("推送点检异常消息到飞书失败，工单号：" + orderNumber + "，" + result.getMsg());
            }
        }
        return true;
    }


    @Override
    public boolean sendMaintenanceExceptionMessage() {
        List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.findMaintenanceExceptionOrder();
        if (maintenanceOrderList == null || maintenanceOrderList.size() == 0)
        {
            log.warn("没有需要推送的保养异常工单");
            return true;
        }
        for(int i=0; i<maintenanceOrderList.size(); i++)
        {
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
            contentStringBuffer.append("工单号：" +orderNumber + "  \n");
            contentStringBuffer.append("资产编码：" +mchCode + "  \n");
            contentStringBuffer.append("设备编号：" +equipNumber + "  \n");
            contentStringBuffer.append("设备名称：" +mchName + "  \n");
            contentStringBuffer.append("存在保养项异常：  \n");

            List<MaintenanceOrderItem> maintenanceOrderItemList = maintenanceOrder.getMaintenanceOrderItemList();

            for(int j=0; j<maintenanceOrderItemList.size(); j++)
            {
                MaintenanceOrderItem maintenanceOrderItem = maintenanceOrderItemList.get(j);
                if(maintenanceOrderItem.getIsException() != null && maintenanceOrderItem.getIsException() == 1)
                {
                    BigDecimal minValue = maintenanceOrderItem.getMinValue();
                    BigDecimal maxValue = maintenanceOrderItem.getMaxValue();
                    BigDecimal actualValue = maintenanceOrderItem.getActualValue();
                    contentStringBuffer.append(maintenanceOrderItem.getMaintenanceItem() +
                            "：参数范围值为" + minValue.stripTrailingZeros().toPlainString()
                            + "到" + maxValue.stripTrailingZeros().toPlainString() +
                            "，实际值为" + (actualValue != null ? actualValue.stripTrailingZeros().toPlainString() : "")  + "  \n");
                }
            }
            contentStringBuffer.append("请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if(result.isSuccess()){
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

            }
            else{
                log.error("推送保养异常消息到飞书失败，工单号：" + orderNumber + "，" + result.getMsg());
            }
        }
        return true;
    }

    @Override
    public boolean sendInspectionTimeoutMessage() {
        List<String> dutyPersonIdList = inspectionOrderService.findInspectionTimeoutOrderDutyPersonIdList();
        if (dutyPersonIdList == null || dutyPersonIdList.size() == 0)
        {
            log.warn("没有需要推送的点检超时工单");
            return true;
        }

        for(int i=0; i<dutyPersonIdList.size(); i++)
        {
            String dutyPersonId = dutyPersonIdList.get(i);
            List<InspectionOrder> inspectionOrderList = inspectionOrderService.findInspectionTimeoutOrderByDutyPersonId(dutyPersonId);
            if(inspectionOrderList == null || inspectionOrderList.size() == 0)
            {
                continue;
            }

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG设备点检超时通知**  \n");
            for(int j=0; j<inspectionOrderList.size(); j++)
            {
                InspectionOrder inspectionOrder = inspectionOrderList.get(j);
                String mchCode = inspectionOrder.getMchCode();
                String orderNumber = inspectionOrder.getOrderNumber();

                contentStringBuffer.append("工单号：" +orderNumber + "，资产编码：" +mchCode + "  \n");
            }

            contentStringBuffer.append("未在班次内按时进行点检，请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if(result.isSuccess()){
                for(InspectionOrder inspectionOrder : inspectionOrderList)
                {
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

            }
            else{
                log.error("推送点检超时消息到飞书失败，责任人：" + dutyPersonId + "，" + result.getMsg());
            }
        }
        return true;
    }

    @Override
    public boolean sendMaintenanceTimeoutMessage() {
        List<String> dutyPersonIdList = maintenanceOrderService.findMaintenanceTimeoutOrderDutyPersonIdList();
        if (dutyPersonIdList == null || dutyPersonIdList.size() == 0)
        {
            log.warn("没有需要推送的保养超时工单");
            return true;
        }
        for(int i=0; i<dutyPersonIdList.size(); i++)
        {
            String dutyPersonId = dutyPersonIdList.get(i);
            List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.findMaintenanceTimeoutOrderByDutyPersonId(dutyPersonId);
            if(maintenanceOrderList == null || maintenanceOrderList.size() == 0)
            {
                continue;
            }

            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**WLG设备保养超时通知**  \n");

            for(MaintenanceOrder maintenanceOrder : maintenanceOrderList)
            {
                String mchCode = maintenanceOrder.getMchCode();
                String orderNumber = maintenanceOrder.getOrderNumber();

                contentStringBuffer.append("工单号：" +orderNumber + "，资产编码：" +mchCode + "  \n");
            }

            contentStringBuffer.append("未按时保养，请注意处理！");

            String content = contentStringBuffer.toString();

            FeishuMessage feishuMessage = new FeishuMessage();
            feishuMessage.setSendType(RECEIVE_TYPE);
            feishuMessage.setSendId(dutyPersonId);
            feishuMessage.setContent(content);

            Result result = notificationProvider.sendFeishuNotification(feishuMessage);
            if(result.isSuccess()){
                for(MaintenanceOrder maintenanceOrder : maintenanceOrderList) {
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

            }
            else{
                log.error("推送保养超时消息到飞书失败，责任人：" + dutyPersonId + "，" + result.getMsg());
            }
        }
        return true;
    }
}
