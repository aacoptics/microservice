package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.constant.MessageTypeConstants;
import com.aacoptics.wlg.equipment.constant.NotificationStatusConstants;
import com.aacoptics.wlg.equipment.entity.po.FeishuMessage;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import com.aacoptics.wlg.equipment.entity.po.MessageHistory;
import com.aacoptics.wlg.equipment.provider.NotificationProvider;
import com.aacoptics.wlg.equipment.service.InspectionOrderService;
import com.aacoptics.wlg.equipment.service.MaintenanceOrderService;
import com.aacoptics.wlg.equipment.service.MessageHistoryService;
import com.aacoptics.wlg.equipment.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


    @Override
    public boolean sendInspectionExceptionMessage() {
        List<InspectionOrder> inspectionOrderList = inspectionOrderService.findInspectionExceptionOrder();
        if (inspectionOrderList == null || inspectionOrderList.size() == 0)
        {
            log.warn("没有需求推送的点检异常工单");
            return true;
        }
        for(int i=0; i<inspectionOrderList.size(); i++)
        {
            InspectionOrder inspectionOrder = inspectionOrderList.get(i);
            inspectionOrder = inspectionOrderService.get(inspectionOrder.getId());
            String dutyPersonId = inspectionOrder.getDutyPersonId();
            String mchCode = inspectionOrder.getMchCode();
            String orderNumber = inspectionOrder.getOrderNumber();


            StringBuffer contentStringBuffer = new StringBuffer();
            contentStringBuffer.append("**点检设备异常通知（测试）**\\n;");
            contentStringBuffer.append("工单号：" +orderNumber + "\\n");
            contentStringBuffer.append("资产编码：" +mchCode + "\\n");
            contentStringBuffer.append("存在点检项异常：\\n");

            List<InspectionOrderItem> inspectionOrderItemList = inspectionOrder.getInspectionOrderItemList();
            for(int j=0; j<inspectionOrderItemList.size(); j++)
            {
                InspectionOrderItem inspectionOrderItem = inspectionOrderItemList.get(i);
                if(inspectionOrderItem.getIsException() == 1)
                {
                    contentStringBuffer.append("参数范围值为" + inspectionOrderItem.getMinValue() + "到" + inspectionOrderItem.getMaxValue() + "；实际参数为" + inspectionOrderItem.getActualValue() + "\\n");
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
}
