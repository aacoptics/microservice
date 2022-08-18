package com.aacoptics.notification.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.FeishuUser;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.UmsContent;
import com.aacoptics.notification.entity.po.UmsContentSub;
import com.aacoptics.notification.entity.vo.MarkdownGroupMessage;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.provider.DingTalkApi;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.service.*;
import com.aacoptics.notification.utils.DingTalkUtil;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.spire.xls.Worksheet;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    @Resource
    UmsContentService umsContentService;

    @Resource
    UmsContentSubService umsContentSubService;

    @Resource
    FeishuApi feishuApi;

    @Resource
    RobotService robotService;

    @Resource
    DingTalkApi dingTalkApi;

    @Resource
    FeishuService feishuService;

    @Override
    public void sendHandledMessage(NotificationEntity notificationEntity) throws BusinessException {
        List<UmsContent> messageBatches;
        if (StrUtil.isBlank(notificationEntity.getBatchId())) {
            messageBatches = umsContentService.getUmsContent(notificationEntity.getPlanKey());
        } else {
            messageBatches = umsContentService.getUmsContentByBatchId(notificationEntity.getBatchId());
        }
        if (messageBatches.size() <= 0) {
            String msg = "当前没有需要推送的批次号！";
            log.error(msg);
            throw new BusinessException(msg);
        }
        for (UmsContent messageBatch : messageBatches) {
            String markdownGroupMessage = getMarkDownMessage(messageBatch);
            if (markdownGroupMessage == null) {
                String msg = "拼接消息失败，请检查！";
                log.error(msg);
                throw new BusinessException(msg);
            }

            String imageKey = null;
            String fileKey = null;
            if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                try {
                    String tempDir = System.getProperty("java.io.tmpdir");
                    long currentTimeMillis = System.currentTimeMillis();
                    String excelFileName1 = messageBatch.getConTypeDesc() + "-" + currentTimeMillis + ".xlsx";
                    String pngExcelFileName = messageBatch.getConTypeDesc() + "-PNG-" + currentTimeMillis + ".xlsx";
                    String pngFileName = messageBatch.getConTypeDesc() + "-" + currentTimeMillis + ".png";
                    URL url = new URL(messageBatch.getSendFilePath());
                    FileUtils.copyURLToFile(url, new File(tempDir + "/" + excelFileName1));

                    if (StrUtil.isNotEmpty(messageBatch.getSendPicturePath())) {
                        url = new URL(messageBatch.getSendPicturePath());
                        FileUtils.copyURLToFile(url, new File(tempDir + "/" + pngExcelFileName));
                        com.spire.xls.Workbook spireXlsWorkbook = new com.spire.xls.Workbook();
                        spireXlsWorkbook.loadFromFile(tempDir + "/" + pngExcelFileName);
                        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
                        worksheet.saveToImage(tempDir + "/" + pngFileName);
                        imageKey = feishuService.fetchUploadMessageImageKey(tempDir + "/" + pngFileName);
                    }

                    fileKey = feishuService.fetchUploadFileKey(FeishuService.FILE_TYPE_XLS, tempDir + "/" + excelFileName1, 0);
//                        String chatName = "每日毛利率飞书测试";
//                        switch (messageBatch.getConType()) {
//                            case "ums_sop_ri_cost_gp_qas":
//                            case "ums_sop_ri_cost_lens_qas":
//                                chatName = "每日毛利率飞书测试";
//                                break;
//                            case "ums_sop_ri_cost_lens_prd":
//                            case "ums_sop_ri_cost_gp_prd":
//                                chatName = "Lens每日运营指标达成汇报";
//                                break;
//                        }
                } catch (IOException err) {
                    String msg = "解析http文件异常！{" + err.getMessage() + "}";
                    log.error(msg);
                    throw new BusinessException(msg);
                }
            }
            JSONObject cardJson = feishuApi.getMarkdownMessage(markdownGroupMessage, imageKey);
            if (messageBatch.getSendType().equals(SendMessageService.GROUP_MESSAGE)) {
                if (notificationEntity.getMsgTypeInfo() == null || notificationEntity.getMsgTypeInfo().size() <= 0) {
                    String msg = "未配置消息类型，请检查！";
                    log.error(msg);
                    throw new BusinessException(msg);
                }
                List<String> chatNames = notificationEntity.getMsgTypeInfo().stream().map(Robot::getRobotName).collect(Collectors.toList());
                List<Robot> robotList = robotService.findByName(chatNames);
                for (Robot messageTypeInfo : robotList) {
                    String chatId = feishuService.fetchChatIdByRobot(messageTypeInfo.getRobotName());
                    boolean fileResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                            chatId,
                            FeishuService.MSG_TYPE_FILE,
                            JSONUtil.createObj().set("file_key", fileKey));

                    if (!fileResult)
                        throw new BusinessException("推送EXCEL文件失败！批次号：{" + messageBatch.getBatchId() + "}");

                    if (messageTypeInfo.getRobotType().equals(RobotService.GROUP_ROBOT)) {
                        String message = feishuApi.SendGroupMessage(messageTypeInfo.getRobotUrl(), cardJson);
                        JSONObject messageJson;
                        try {
                            messageJson = JSONUtil.parseObj(message);
                        } catch (Exception err) {
                            String msg = "解析返回值失败！{" + err.getMessage() + "}";
                            log.error(msg);
                            throw new BusinessException(msg);
                        }
                        if (messageJson.containsKey("StatusCode") && messageJson.getInt("StatusCode") == 0 && fileResult) {
                            messageBatch.setIsStatus("1");
                            umsContentService.updateById(messageBatch);
                        } else {
                            String errorMsg;
                            if (messageJson.containsKey("msg") && !StringUtils.isEmpty(messageJson.getStr("msg"))) {
                                errorMsg = messageJson.getStr("msg");
                                log.error(errorMsg);
                                throw new BusinessException(errorMsg);
                            }
                        }
                    } else if (messageTypeInfo.getRobotType().equals(RobotService.APPLICATION_ROBOT)) {

                        boolean sendMsgResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                                chatId,
                                FeishuService.MSG_TYPE_INTERACTIVE,
                                cardJson);

                        if (sendMsgResult) {
                            messageBatch.setIsStatus("1");
                            umsContentService.updateById(messageBatch);
                        } else {
                            throw new BusinessException("推送消息失败！批次号：{" + messageBatch.getBatchId() + "}");
                        }
                    }
                }
            } else if (messageBatch.getSendType().equals(SendMessageService.PERSONAL_MESSAGE)) {
                if (StrUtil.isBlank(messageBatch.getUserNum()))
                    throw new BusinessException("推送消息失败！人员工号为空，批次号：{" + messageBatch.getBatchId() + "}");
                final FeishuUser feishuUser = feishuService.getFeishuUser(messageBatch.getUserNum());
                if (ObjectUtil.isNull(feishuUser))
                    throw new BusinessException("推送消息失败！飞书用户不存在，批次号：{" + messageBatch.getBatchId() + "}");

                boolean resultByFile = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID,
                        feishuUser.getUserId(),
                        FeishuService.MSG_TYPE_FILE,
                        JSONUtil.createObj().set("file_key", fileKey));
                if (!resultByFile)
                    throw new BusinessException("推送EXCEL文件失败！批次号：{" + messageBatch.getBatchId() + "}");

                boolean resultBySendMsg = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID,
                        feishuUser.getUserId(),
                        FeishuService.MSG_TYPE_INTERACTIVE,
                        cardJson);

                if (resultBySendMsg) {
                    messageBatch.setIsStatus("1");
                    umsContentService.updateById(messageBatch);
                } else {
                    throw new BusinessException("推送消息失败！批次号：{" + messageBatch.getBatchId() + "}");
                }
            }
        }
    }

    @Override
    public String getMarkDownMessage(UmsContent messageBatch) {
        List<UmsContentSub> messageValues = umsContentSubService.getUmsContentSub(messageBatch.getBatchId());

        if (messageValues.size() > 0) {
            MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
            markdownGroupMessage.setTitle(messageBatch.getConTypeDesc());
            for (UmsContentSub messageValue : messageValues) {
                if (StrUtil.isBlank(messageValue.getValueDesc())) {
                    markdownGroupMessage.addBlankLine();
                    continue;
                }

                String msgContent = messageValue.getValueDesc();
                if (!StrUtil.isBlank(messageValue.getIsBold()) && messageValue.getIsBold().equals("Y")) {
                    msgContent = markdownGroupMessage.addBlobContent(msgContent);
                }
                if (!StrUtil.isBlank(messageValue.getIsItalics()) && messageValue.getIsItalics().equals("Y")) {
                    msgContent = markdownGroupMessage.addItalics(msgContent);
                }
                if (!StrUtil.isBlank(messageValue.getIsDeleteline()) && messageValue.getIsDeleteline().equals("Y")) {
                    msgContent = markdownGroupMessage.addDeleteLine(msgContent);
                }
                if (!StrUtil.isBlank(messageValue.getIsUnderline()) && messageValue.getIsUnderline().equals("Y")) {
                    msgContent = markdownGroupMessage.addUnderline(msgContent);
                }
                if (!StrUtil.isBlank(messageValue.getIsSeqno())) {
                    msgContent = markdownGroupMessage.addSeqNo(msgContent, messageValue.getIsSeqno());
                } else if (!StrUtil.isBlank(messageValue.getIsList()) && messageValue.getIsList().equals("Y")) {
                    msgContent = markdownGroupMessage.addList(msgContent);
                }
                markdownGroupMessage.addContent(msgContent);
            }
            if (!StrUtil.isBlank(messageBatch.getLinkUrl())) {
                markdownGroupMessage.addContent("[查看详情](" + messageBatch.getLinkUrl() + ")");
            }
            return markdownGroupMessage.toString();
        } else {
            return null;
        }
    }

    @Override
    public Result sendDingTalkNotification(String jobNumber, String title, String content) {
        //获取token
        OapiGettokenResponse oapiGettokenResponse = null;
        try {
            oapiGettokenResponse = dingTalkApi.getAccessToken();
        } catch (ApiException e) {
            return Result.fail(e);
        }
        String accessToken = oapiGettokenResponse.getAccessToken();
        try {
            OapiMessageCorpconversationAsyncsendV2Response res = DingTalkUtil.sendCardCorpConversation(accessToken, 1186196480L
                    , jobNumber, title, content);
            if (res.isSuccess())
                return Result.success();
            else
                return Result.fail(res);
        } catch (ApiException e) {
            return Result.fail(e);
        }
    }
}