package com.aacoptics.sale.service.impl;

import com.aacoptics.sale.entity.*;
import com.aacoptics.sale.mapper.SendSalesDataMapper;
import com.aacoptics.sale.provider.DingTalkApi;
import com.aacoptics.sale.provider.FeishuApi;
import com.aacoptics.sale.provider.JacobProvider;
import com.aacoptics.sale.service.SendSalesDataService;
import com.aacoptics.sale.util.BIRsaEncrypt;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SendSalesDataServiceImpl implements SendSalesDataService {


    @Resource
    DingTalkApi dingTalkApi;

    @Resource
    FeishuApi feishuApi;

    @Resource
    JacobProvider jacobProvider;

    @Resource
    SendSalesDataMapper sendSalesDataMapper;

    /**
     * 发送销售数据工作通知
     *
     * @throws ApiException
     */
    public void sendSalesDataNotification() throws ApiException {
        //获取需要推送的内容
        List<Content> salesDataContentList = sendSalesDataMapper.getSalesContent();
        if (salesDataContentList == null || salesDataContentList.size() == 0) {
            log.info("没有需要发送的通知信息");
            return;
        }

        //获取token
        OapiGettokenResponse oapiGettokenResponse = dingTalkApi.getAccessToken();
        String accessToken = oapiGettokenResponse.getAccessToken();

        for (Content salesDataContent : salesDataContentList) {
            Integer contentId = salesDataContent.getId();
            String title = salesDataContent.getTitle();
            String content = salesDataContent.getContent();
            String tabType = salesDataContent.getTabType();
            String tabDate = salesDataContent.getTabDate();
            String userid = salesDataContent.getUserid(); //需要推送的用户
            String tabUrl = salesDataContent.getTabUrl();
            String adaccount = salesDataContent.getAdaccount(); //域账号

            String flag = salesDataContent.getFlag();//工作通知发送状态
            String dingtalkFlag = salesDataContent.getDingtalkFlag();//代办事项发送状态

            //获取需要推送的用户
//            List<SalesUser> salesUserList = sendSalesDataMapper.getSendUsersByType(tabType);
//            if (salesUserList == null || salesUserList.size() == 0) {
//                log.warn("类型{}未配置推送用户", tabType);
//                return;
//            }
//
//            String tabUrl = "";
//            StringBuffer userIds = new StringBuffer();
//            for (SalesUser salesUser : salesUserList) {
//                tabUrl = salesUser.getTabUrl();
//
//                String userId = salesUser.getUserid();
//                userIds.append(userId + ",");
//            }

            String ssoUrl = tabUrl;
            try {
                String ssoToken = BIRsaEncrypt.getSsoToken(adaccount);
                if(tabUrl.contains("?"))
                {
                    ssoUrl = tabUrl + "&ssoToken=" + ssoToken;
                }
                else
                {
                    ssoUrl = tabUrl + "?ssoToken=" + ssoToken;
                }

                log.info("ssoUrl=" + ssoUrl);
            } catch (Exception exception) {
                log.error("获取单点登录token异常", exception);
            }

            MarkdownCard markdownCard = this.convertContentToMarkdown(content);
            markdownCard.addContent("[查看详情](" + ssoUrl + ")");

            if("0".equals(flag))
            {
                //发送销售数据工作通知
                boolean result = dingTalkApi.sendCardCorpConversation(accessToken, userid, title + "（" + tabDate + "）", markdownCard.toString(), "查看详情", ssoUrl);
                if(result) {
                    //更新发送状态，成功
                    sendSalesDataMapper.updateSalesContentSendFlag(contentId, "1");
                }else
                {
                    //更新发送状态，失败
                    sendSalesDataMapper.updateSalesContentSendFlag(contentId, "2");
                }
            }

            if("0".equals(dingtalkFlag))
            {
                String unionId = dingTalkApi.getUnionId(accessToken, userid);
                if(StringUtils.isEmpty(unionId))
                {
                    //更新发送状态-失败
                    sendSalesDataMapper.updateSalesContentDingtalkFlag(contentId, "4");
                    continue;
                }
                //创建待办事项
                try {
                    dingTalkApi.createDingtalkTodo(accessToken, unionId, "cost_" + contentId, title+ "（" + tabDate + "）", content, ssoUrl);
                    //更新发送状态-已发送
                    sendSalesDataMapper.updateSalesContentDingtalkFlag(contentId, "1");
                } catch (Exception exception) {
                    log.error("创建待办事项异常, contentId=" + contentId, exception);
                }
            }
            log.info("销售数据【{}】推送到用户【{}】工作通知", contentId, userid);

        }

    }


    @Override
    public void deleteSalesDataTodoTask() throws ApiException {
        List<Content> salesDataDeleteTodoTaskList = sendSalesDataMapper.getDeleteTodoTask();
        if (salesDataDeleteTodoTaskList == null || salesDataDeleteTodoTaskList.size() == 0) {
            return;
        }

        //获取token
        OapiGettokenResponse oapiGettokenResponse = dingTalkApi.getAccessToken();
        String accessToken = oapiGettokenResponse.getAccessToken();
        for(Content deleteTodoTask : salesDataDeleteTodoTaskList)
        {
            Integer contentId = deleteTodoTask.getId();
            String userid = deleteTodoTask.getUserid(); //需要推送的用户

            String unionId = dingTalkApi.getUnionId(accessToken, userid);
            try {
                dingTalkApi.deleteDingtalkTodoTask(accessToken, unionId, "cost_" + contentId);

                sendSalesDataMapper.updateSalesContentDingtalkFlag(contentId, "3");
            } catch (Exception exception) {
                log.error("删除待办事项异常, contentId=" + contentId, exception);
            }
        }
    }

    /**
     * 转换字符串为markdown格式
     *
     * @param content
     * @return
     */
    private MarkdownCard convertContentToMarkdown(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        String[] contentArray = content.split(";");

        MarkdownCard markdownCard = new MarkdownCard();
        markdownCard.setTitle(contentArray[0]);

        for (int i = 1; i < contentArray.length; i++) {
            markdownCard.addContent(contentArray[i]);
        }

        return markdownCard;
    }

    @Override
    public void sendSalesDataGroupMessage() throws ApiException {
        LocalTime currentTime = LocalTime.now();

        List<Map<String, String>> batchList = sendSalesDataMapper.getSalesDataBatch();
        if (batchList == null || batchList.size() == 0) {
            log.info("没有需要发送到钉钉群的销售数据");
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        DecimalFormat percentDecimalFormat = new DecimalFormat("0.0%");
        for (Map<String, String> batchMap : batchList) {
            String title = batchMap.get("TITLE");
            String batchId = batchMap.get("BATCH");
            String titleTime = batchMap.get("TITLE_TIME");
            String tabType = batchMap.get("TAB_TYPE");

            //获取销售数据明细
            List<ProductContent> productContentList = sendSalesDataMapper.getSalesProductContentByBatch(batchId);

            MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
            markdownGroupMessage.setTitle(title);
            markdownGroupMessage.addContent("日期：" + titleTime);

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateParam = LocalDate.parse(titleTime, df);
            DateTimeFormatter dfZh = DateTimeFormatter.ofPattern("M月d日");
            String voiceStr = dateParam.format(dfZh) + "\n";
            for (ProductContent productContent : productContentList) {
                String productType = productContent.getTabProductType();


                String dayTabProductType = productContent.getDayTabProductType();
                String dayShipQty = productContent.getDayShipQty() != null ? decimalFormat.format(productContent.getDayShipQty()) : "-";
                String dayShipPlanQty = productContent.getDayShipPlanQty() != null ? decimalFormat.format(productContent.getDayShipPlanQty()) : "-";
                String dayShipRate = productContent.getDayShipQtyRate() != null ? percentDecimalFormat.format(productContent.getDayShipQtyRate()) : "-";

                String mtdTabProductType = productContent.getMtdTabProductType();
                String mtdShipQty = productContent.getMtdShipQty() != null ? decimalFormat.format(productContent.getMtdShipQty()) : "-";
                String mtdShipPlanQty = productContent.getMtdShipPlanQty() != null ? decimalFormat.format(productContent.getMtdShipPlanQty()) : "-";
                String mtdShipRate = productContent.getMtdShipQtyRate() != null ? percentDecimalFormat.format(productContent.getMtdShipQtyRate()) : "-";

                String subProductType = productContent.getSubTabProductType();
                String subShipQty = productContent.getSubShipQty() != null ? decimalFormat.format(productContent.getSubShipQty()) : "-";
                String subShipAmount = productContent.getSubShipAmount() != null ? decimalFormat.format(productContent.getSubShipAmount()) : "-";

                String subStandardProductType = productContent.getSubStandardTabProductType();
                String subStandardShipQty = productContent.getSubStandardShipQty() != null ? decimalFormat.format(productContent.getSubStandardShipQty()) : "-";
                String subStandardShipAmount = productContent.getSubStandardShipAmount() != null ? decimalFormat.format(productContent.getSubStandardShipAmount()) : "-";

                String shipQty = productContent.getShipQty() != null ? decimalFormat.format(productContent.getShipQty()) : "-";
                String shipAmount = productContent.getShipAmount() != null ? decimalFormat.format(productContent.getShipAmount()) : "-";
                String shipPlanQty = productContent.getShipPlanQty() != null ? decimalFormat.format(productContent.getShipPlanQty()) : "-";
                String shipPlanAmount = productContent.getShipPlanAmount() != null ? decimalFormat.format(productContent.getShipPlanAmount()) : "-";
                String shipQtyRate = productContent.getShipQtyRate() != null ? percentDecimalFormat.format(productContent.getShipQtyRate()) : "-";
                String shipAmountRate = productContent.getShipAmountRate() != null ? percentDecimalFormat.format(productContent.getShipAmountRate()) : "-";

                String fcstMonQty = productContent.getFcstMonQty() != null ? decimalFormat.format(productContent.getFcstMonQty()) : "-";
                String fcstMonQtyRate = productContent.getFcstMonQtyRate() != null ? percentDecimalFormat.format(productContent.getFcstMonQtyRate()) : "-";
                String fcstMonAmount = productContent.getFcstMonAmount() != null ? decimalFormat.format(productContent.getFcstMonAmount()) : "-";
                String fcstMonAmountRate = productContent.getFcstMonAmountRate() != null ? percentDecimalFormat.format(productContent.getFcstMonAmountRate()) : "-";

                String subDayShipGpQty = productContent.getSubDayShipGpQty() != null ? decimalFormat.format(productContent.getSubDayShipGpQty()) : "-";
                String subMtdShipGpQty = productContent.getSubMtdShipGpQty() != null ? decimalFormat.format(productContent.getSubMtdShipGpQty()) : "-";

                if ("汇总".equals(productType)) {
                    markdownGroupMessage.addBlobContent(productType);
                    if (!StringUtils.isEmpty(dayTabProductType)) {
                        if ("-".equals(dayShipPlanQty)) {
                            markdownGroupMessage.addBlobContent(dayTabProductType + "预测出货数量：" + dayShipPlanQty);
                        } else {
                            markdownGroupMessage.addBlobContent(dayTabProductType + "预测出货数量：" + dayShipPlanQty + " K");
                        }

                        if ("-".equals(dayShipQty)) {
                            markdownGroupMessage.addBlobContent(dayTabProductType + "实际出货数量：" + dayShipQty);
                        } else {
                            markdownGroupMessage.addBlobContent(dayTabProductType + "实际出货数量：" + dayShipQty + " K");
                        }

                        if (!"-".equals(subDayShipGpQty)) {
                            markdownGroupMessage.addContent("其中G+P出货数量：" + subDayShipGpQty + " K");
                        }

                        markdownGroupMessage.addBlobContent(dayTabProductType + "出货数量达成：" + dayShipRate);
                        markdownGroupMessage.addBlankLine();
                    }

                    markdownGroupMessage.addBlobContent("当月董事会目标出货数量：" + shipPlanQty + " K");

                    if(!"-".equals(fcstMonQty)){
                        markdownGroupMessage.addContent("当月预测目标出货数量：" + fcstMonQty + " K");
                    }

                    //MTD
                    if(!StringUtils.isEmpty(mtdTabProductType))
                    {
                        if("-".equals(mtdShipPlanQty))
                        {
                            markdownGroupMessage.addBlobContent(mtdTabProductType + "预测目标出货数量：" + mtdShipPlanQty);
                        }
                        else
                        {
                            markdownGroupMessage.addBlobContent(mtdTabProductType + "预测目标出货数量：" + mtdShipPlanQty + " K");
                        }
                        if("-".equals(mtdShipQty))
                        {
                            markdownGroupMessage.addBlobContent(mtdTabProductType + "实际出货数量：" + mtdShipQty);
                        }
                        else
                        {
                            markdownGroupMessage.addBlobContent(mtdTabProductType + "实际出货数量：" + mtdShipQty + " K");
                        }

                        if (!"-".equals(subMtdShipGpQty)) {
                            markdownGroupMessage.addContent("其中G+P出货数量：" + subMtdShipGpQty + " K");
                        }


                    }
                    if(!StringUtils.isEmpty(subProductType))
                    {
                        markdownGroupMessage.addBlobContent(subProductType + "出货数量：" + subShipQty + " K");
                    }
                    if(!StringUtils.isEmpty(subStandardProductType) && !"-".equals(subStandardShipQty))
                    {
                        markdownGroupMessage.addBlobContent(subStandardProductType + "出货数量：" + subStandardShipQty + " K");
                    }
                    if(!StringUtils.isEmpty(mtdTabProductType))
                    {
                        markdownGroupMessage.addBlobContent(mtdTabProductType + "预测出货数量达成：" + mtdShipRate);
                    }
//                    markdownGroupMessage.addBlobContent("当月实际出货数量：" + shipQty + " K");
                    markdownGroupMessage.addBlobContent("当月董事会目标出货数量达成：" + shipQtyRate);

                    if(!"-".equals(fcstMonQtyRate)){
                        markdownGroupMessage.addBlobContent("当月预测目标出货数量达成：" + fcstMonQtyRate);
                    }

                    markdownGroupMessage.addBlobContent("当月董事会目标出货金额：" + shipPlanAmount + " K");

                    if(!"-".equals(fcstMonAmount)){
                        markdownGroupMessage.addContent("当月预测目标出货金额：" + fcstMonAmount + " K");
                    }

                    markdownGroupMessage.addBlobContent("当月实际出货金额：" + shipAmount + " K");
                    if(!StringUtils.isEmpty(subProductType))
                    {
                        markdownGroupMessage.addBlobContent(subProductType + "出货金额：" + subShipAmount + " K");
                    }
                    if(!StringUtils.isEmpty(subStandardProductType) && !"-".equals(subStandardShipAmount))
                    {
                        markdownGroupMessage.addBlobContent(subStandardProductType + "出货金额：" + subStandardShipAmount + " K");
                    }
                    markdownGroupMessage.addBlobContent("当月董事会目标出货金额达成：" + shipAmountRate);

                    if(!"-".equals(fcstMonAmount)){
                        markdownGroupMessage.addBlobContent("当月预测目标出货金额达成：" + fcstMonAmountRate);
                    }
                }
                else {
                    markdownGroupMessage.addBlobContent(productType);
                    if(productType.equals("Lens")){
                        voiceStr += "手机镜头产品线\n";
                    }else{
                        voiceStr += productType + "产品线\n";
                    }
                    if(!StringUtils.isEmpty(dayTabProductType)) {
                        if("-".equals(dayShipPlanQty))
                        {
                            markdownGroupMessage.addContent(dayTabProductType + "预测出货数量：" + dayShipPlanQty);
                        } else {
                            markdownGroupMessage.addContent(dayTabProductType + "预测出货数量：" + dayShipPlanQty + " K");
                            voiceStr += dayTabProductType + "预测出货数量：" + dayShipPlanQty + " K\n";
                        }
                        if ("-".equals(dayShipQty)) {
                            markdownGroupMessage.addContent(dayTabProductType + "实际出货数量：" + dayShipQty);
                        } else {
                            markdownGroupMessage.addContent(dayTabProductType + "实际出货数量：" + dayShipQty + " K");
                            voiceStr += dayTabProductType + "实际出货数量：" + dayShipQty + " K\n";
                        }

                        if (!"-".equals(subDayShipGpQty)) {
                            markdownGroupMessage.addContent("其中G+P出货数量：" + subDayShipGpQty + " K");
                        }


                        markdownGroupMessage.addBlobContent(dayTabProductType + "出货数量达成：" + dayShipRate);
                        voiceStr += dayTabProductType + "出货数量达成：" + dayShipRate + "\n";
                        markdownGroupMessage.addBlankLine();
                    }
                    markdownGroupMessage.addContent("当月董事会目标出货数量：" + shipPlanQty + " K");

                    if(!"-".equals(fcstMonQty)){
                        markdownGroupMessage.addContent("当月预测目标出货数量：" + fcstMonQty + " K");
                        voiceStr += "当月预测目标出货数量：" + fcstMonQty + " K\n";
                    }
                    //MTD
                    if(!StringUtils.isEmpty(mtdTabProductType))
                    {
                        if("-".equals(mtdShipPlanQty))
                        {
                            markdownGroupMessage.addContent(mtdTabProductType + "预测目标出货数量：" + mtdShipPlanQty);
                        }
                        else
                        {
                            markdownGroupMessage.addContent(mtdTabProductType + "预测目标出货数量：" + mtdShipPlanQty + " K");
                        }
                        if("-".equals(mtdShipQty))
                        {
                            markdownGroupMessage.addContent(mtdTabProductType + "实际出货数量：" + mtdShipQty);
                        }
                        else
                        {
                            markdownGroupMessage.addContent(mtdTabProductType + "实际出货数量：" + mtdShipQty + " K");
                            voiceStr += mtdTabProductType + "实际出货数量：" + mtdShipQty + " K\n";
                        }
                        if (!"-".equals(subMtdShipGpQty)) {
                            markdownGroupMessage.addContent("其中G+P出货数量：" + subMtdShipGpQty + " K");
                        }
                    }


//                    markdownGroupMessage.addContent("当月实际出货数量：" + shipQty + " K");
                    if(!StringUtils.isEmpty(subProductType))
                    {
                        markdownGroupMessage.addContent(subProductType + "出货数量：" + subShipQty + " K");
                    }
                    if(!StringUtils.isEmpty(subStandardProductType) && !"-".equals(subStandardShipQty))
                    {
                        markdownGroupMessage.addContent(subStandardProductType + "出货数量：" + subStandardShipQty + " K");
                    }
                    if(!StringUtils.isEmpty(mtdTabProductType))
                    {
                        markdownGroupMessage.addContent(mtdTabProductType + "预测出货数量达成：" + mtdShipRate);
                    }

                    markdownGroupMessage.addBlobContent("当月董事会目标出货数量达成：" + shipQtyRate);

                    if(!"-".equals(fcstMonQtyRate)){
                        markdownGroupMessage.addBlobContent("当月预测目标出货数量达成：" + fcstMonQtyRate);
                        voiceStr += "当月预测目标出货数量达成：" + fcstMonQtyRate + "\n";
                    }

                    markdownGroupMessage.addContent("当月董事会目标出货金额：" + shipPlanAmount + " K");

                    if(!"-".equals(fcstMonAmount)){
                        markdownGroupMessage.addContent("当月预测目标出货金额：" + fcstMonAmount + " K");
                    }

                    markdownGroupMessage.addContent("当月实际出货金额：" + shipAmount + " K");
                    if (!StringUtils.isEmpty(subProductType)) {
                        markdownGroupMessage.addContent(subProductType + "出货金额：" + subShipAmount + " K");
                    }
                    if(!StringUtils.isEmpty(subStandardProductType) && !"-".equals(subStandardShipAmount))
                    {
                        markdownGroupMessage.addContent(subStandardProductType + "出货金额：" + subStandardShipAmount + " K");
                    }
                    markdownGroupMessage.addBlobContent("当月董事会目标出货金额达成：" + shipAmountRate);

                    if(!"-".equals(fcstMonAmount)){
                        markdownGroupMessage.addBlobContent("当月预测目标出货金额达成：" + fcstMonAmountRate);
                    }
                    markdownGroupMessage.addBlankLine();
                }
            }
            //获取详情URL
            String tabUrl = sendSalesDataMapper.getUrlByTabType(tabType);
            if (StringUtils.isEmpty(tabUrl)) {
                log.warn("类型【{}】详情地址URL未配置", tabType);
            } else {
                markdownGroupMessage.addContent("[查看详情](" + tabUrl + ")");
            }
            List<Map<String, Object>> robotMapList = sendSalesDataMapper.getRobotUrlByTabType(tabType);
            if (robotMapList == null || robotMapList.size() == 0) {
                log.error("类型{}机器人URL未配置，请确认");
                continue;
            }

            int sendCount = 0; //发送记录数
            for (Map<String, Object> robotMap : robotMapList) {
                Integer robotId = Integer.valueOf(robotMap.get("ID") + "");
                String robotUrl = robotMap.get("ROBOT_URL") != null ? robotMap.get("ROBOT_URL") + "" : "";
                String sendTimeStr = robotMap.get("SEND_TIME") != null ? robotMap.get("SEND_TIME") + "" : "";
                String robotName = robotMap.get("REMARKS") != null ? robotMap.get("REMARKS") + "" : "";
                //校验是否已发送
                List<Map<String, String>> sendHistoryList = sendSalesDataMapper.getSendHistoryByBatchAndRobot(batchId, robotId);
                if (sendHistoryList != null && sendHistoryList.size() > 0) {
                    sendCount++;
                    continue;
                }

                //校验当前时间是否在配置时间之后，如果是，则发送
                if (!StringUtils.isEmpty(sendTimeStr)) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime sendTime = LocalTime.parse(sendTimeStr, dateTimeFormatter);
                    if (currentTime.isBefore(sendTime)) {
                        continue;
                    }
                }

                if (robotUrl.contains("feishu")) {
                    String message = feishuApi.SendGroupMessage(robotUrl, markdownGroupMessage.toString());
                    log.debug(message);
                    JSONObject messageJson = new JSONObject();
                    try {
                        messageJson = JSONObject.parseObject(message);
                    } catch (Exception err) {
                        sendSalesDataMapper.saveSendHistory(batchId, robotId, "false", null);
                        log.error("解析返回值失败！{}", err.getMessage());
                    }
                    if (messageJson.containsKey("StatusCode") && messageJson.getInteger("StatusCode") == 0) {
                        sendSalesDataMapper.saveSendHistory(batchId, robotId, "true", null);
                        FeishuVoiceFileInfo feishuVoiceFileInfo = new FeishuVoiceFileInfo();
                        feishuVoiceFileInfo.setGroupName(robotName);
                        feishuVoiceFileInfo.setMessage(voiceStr);
                        jacobProvider.sendFeishuVoiceMsg(feishuVoiceFileInfo);
                    } else {
                        String errorMsg = null;
                        if (messageJson.containsKey("msg") && !StringUtils.isEmpty(messageJson.getString("msg"))) {
                            errorMsg = messageJson.getString("msg");
                        }
                        sendSalesDataMapper.saveSendHistory(batchId, robotId, "false", errorMsg);
                    }
                } else {
                    Map<String, String> resultMap = dingTalkApi.sendGroupRobotMessage(robotUrl, title, markdownGroupMessage.toString());
                    JSONObject resultMapJson = (JSONObject)JSONObject.toJSON(resultMap);
                    log.debug(JSONObject.toJSONString(resultMapJson));
                    String result = resultMap.get("result");
                    String message = resultMap.get("message");
                    if (!StringUtils.isEmpty(message) && message.length() > 1024) {
                        message = message.substring(1024);
                    }
                    sendSalesDataMapper.saveSendHistory(batchId, robotId, result, message);
                }
                sendCount++;
            }
            //如果所有已配置的群都已发送，更新发送状态
            if (sendCount >= robotMapList.size()) {
                sendSalesDataMapper.updateSalesProductContentSendFlag(batchId);
            }
            log.info("销售数据【{}】推送到群", batchId);
        }
    }
}