package com.aacoptics.notification.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.*;
import com.aacoptics.notification.entity.vo.FeishuMessage;
import com.aacoptics.notification.entity.vo.MarkdownGroupMessage;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.exception.BusinessException;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.service.*;
import com.spire.xls.Worksheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
    UmsContentSubDaibanService umsContentSubDaibanService;

    @Resource
    UmsContentFeishuMsgHistoryService umsContentFeishuMsgHistoryService;

    @Resource
    NotificationJobSubscriptionService notificationJobSubscriptionService;


    @Resource
    FeishuApi feishuApi;

    @Resource
    RobotService robotService;

    @Resource
    FeishuService feishuService;

    @Override
    public void sendHandledMessage(NotificationEntity notificationEntity) throws BusinessException {
        List<UmsContent> messageBatches;
        if (StrUtil.isBlank(notificationEntity.getBatchId())) {
            messageBatches = umsContentService.getUmsContent(notificationEntity.getPlanKey());
        } else {
            messageBatches = umsContentService.getUmsContentByBatchId(notificationEntity.getPlanKey(), notificationEntity.getBatchId());
        }
        if (messageBatches.size() <= 0) {
            String msg = "当前没有需要推送的批次号！";
            throw new BusinessException(msg);
        }

        for (UmsContent messageBatch : messageBatches) {

            if (messageBatch.getSendType().equals(SendMessageService.TASK_MESSAGE)) {
                try {
                    createTask(messageBatch.getBatchId());
                    messageBatch.setIsStatus("1");
                    messageBatch.setUpdatedTime(LocalDateTime.now());
                    umsContentService.updateById(messageBatch);
                } catch (Exception err) {
                    // throw new BusinessException("创建任务失败！批次号：{" + messageBatch.getBatchId() + "}");
                    messageBatch.setIsStatus("2");
                    messageBatch.setUpdatedTime(LocalDateTime.now());
                    umsContentService.updateById(messageBatch);
                    return;
                }
            } else {
                String markdownGroupMessage = getMarkDownMessage(messageBatch);
                if (markdownGroupMessage == null) {
                    String msg = "拼接消息失败，请检查！";
                    log.error(msg);
                    // throw new BusinessException(msg);
                    messageBatch.setIsStatus("2");
                    messageBatch.setUpdatedTime(LocalDateTime.now());
                    umsContentService.updateById(messageBatch);
                    return;
                }

                String imageKey = null;
                String fileKey = null;
                String tempDir = System.getProperty("java.io.tmpdir");
                long currentTimeMillis = System.currentTimeMillis();
                if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                    try {

                        String excelFileName1 = (StrUtil.isBlank(messageBatch.getSendFileName()) ?
                                messageBatch.getConTypeDesc()
                                : messageBatch.getSendFileName())
                                + "-" + currentTimeMillis + ".xlsx";
                        URL url = new URL(messageBatch.getSendFilePath());
                        FileUtils.copyURLToFile(url, new File(tempDir + "/" + excelFileName1));
                        fileKey = feishuService.fetchUploadFileKey(FeishuService.FILE_TYPE_XLS, tempDir + "/" + excelFileName1, 0);
                    } catch (IOException err) {
                        String msg = "解析http文件异常！{" + err.getMessage() + "}";
                        log.error(msg);
                        // throw new BusinessException(msg);
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }
                }
                if (!StrUtil.isBlank(messageBatch.getSendPicturePath())) {
                    try {
                        String pngExcelFileName = (StrUtil.isBlank(messageBatch.getSendFileName()) ?
                                messageBatch.getConTypeDesc()
                                : messageBatch.getSendFileName())
                                + "-PNG-" + currentTimeMillis + ".xlsx";
                        String pngFileName = (StrUtil.isBlank(messageBatch.getSendFileName()) ?
                                messageBatch.getConTypeDesc()
                                : messageBatch.getSendFileName())
                                + "-" + currentTimeMillis + ".png";
                        URL url = new URL(messageBatch.getSendPicturePath());
                        FileUtils.copyURLToFile(url, new File(tempDir + "/" + pngExcelFileName));
                        com.spire.xls.Workbook spireXlsWorkbook = new com.spire.xls.Workbook();
                        spireXlsWorkbook.loadFromFile(tempDir + "/" + pngExcelFileName);
                        Worksheet worksheet = spireXlsWorkbook.getWorksheets().get(0);
                        worksheet.saveToImage(tempDir + "/" + pngFileName);
                        imageKey = feishuService.fetchUploadMessageImageKey(tempDir + "/" + pngFileName);
                    } catch (IOException err) {
                        String msg = "解析http文件异常！{" + err.getMessage() + "}";
                        log.error(msg);
                        // throw new BusinessException(msg);
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }
                }
                JSONObject cardJson = feishuApi.getMarkdownMessage(markdownGroupMessage, imageKey);
                if (messageBatch.getSendType().equals(SendMessageService.GROUP_MESSAGE)) {
                    if (notificationEntity.getMsgTypeInfo() == null || notificationEntity.getMsgTypeInfo().size() <= 0) {
                        String msg = "未配置消息类型，请检查！";
                        log.error(msg);
                        // throw new BusinessException(msg);
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }
                    List<Long> robotIds = notificationEntity.getMsgTypeInfo().stream().map(Robot::getId).collect(Collectors.toList());
                    List<Robot> robotList = robotService.findByIds(robotIds);
                    for (Robot messageTypeInfo : robotList) {
                        String chatId = feishuService.fetchChatIdByRobot(messageTypeInfo.getRobotName());

                        if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                            JSONObject fileResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID, chatId, FeishuService.MSG_TYPE_FILE, JSONUtil.createObj().set("file_key", fileKey));

                            if (fileResult.get("code", Integer.class) != 0) {
                                // throw new BusinessException("推送EXCEL文件失败！批次号：{" + messageBatch.getBatchId() + "}");
                                messageBatch.setIsStatus("2");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                                return;
                            }

                            logFeishuMsg(fileResult, messageBatch);
                        }

                        if (messageTypeInfo.getRobotType().equals(RobotService.GROUP_ROBOT)) {
                            String message = feishuApi.SendGroupMessage(messageTypeInfo.getRobotUrl(), cardJson);
                            JSONObject messageJson;
                            try {
                                messageJson = JSONUtil.parseObj(message);
                            } catch (Exception err) {
                                String msg = "解析返回值失败！{" + err.getMessage() + "}";
                                // throw new BusinessException(msg);
                                messageBatch.setIsStatus("2");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                                return;
                            }
                            if (messageJson.containsKey("StatusCode") && messageJson.getInt("StatusCode") == 0) {
                                messageBatch.setIsStatus("1");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                            } else {
                                String errorMsg;
                                if (messageJson.containsKey("msg") && !StringUtils.isEmpty(messageJson.getStr("msg"))) {
                                    errorMsg = messageJson.getStr("msg");
                                    // throw new BusinessException(errorMsg);
                                    messageBatch.setIsStatus("2");
                                    messageBatch.setUpdatedTime(LocalDateTime.now());
                                    umsContentService.updateById(messageBatch);
                                    return;
                                }
                            }
                        } else if (messageTypeInfo.getRobotType().equals(RobotService.APPLICATION_ROBOT)) {

                            JSONObject sendMsgResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID, chatId, FeishuService.MSG_TYPE_INTERACTIVE, cardJson);

                            if (sendMsgResult.get("code", Integer.class) == 0) {
                                messageBatch.setIsStatus("1");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                                logFeishuMsg(sendMsgResult, messageBatch);
                            } else {
                                // throw new BusinessException("推送消息失败！批次号：{" + messageBatch.getBatchId() + "}");
                                messageBatch.setIsStatus("2");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                                return;
                            }
                        }

                        if (!StrUtil.isBlank(messageBatch.getIsYunUrl())) {
                            JSONObject fileResult = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID,
                                    chatId, FeishuService.MSG_TYPE_TEXT, JSONUtil.createObj().set("text", messageBatch.getIsYunUrl()));

                            if (fileResult.get("code", Integer.class) != 0) {
                                // throw new BusinessException("推送云文档URL失败！批次号：{" + messageBatch.getBatchId() + "}");
                                messageBatch.setIsStatus("2");
                                messageBatch.setUpdatedTime(LocalDateTime.now());
                                umsContentService.updateById(messageBatch);
                                return;
                            }

                            logFeishuMsg(fileResult, messageBatch);
                        }
                    }
                } else if (messageBatch.getSendType().equals(SendMessageService.PERSONAL_MESSAGE)) {
                    if (StrUtil.isBlank(messageBatch.getUserNum())) {
                        // throw new BusinessException("推送消息失败！人员工号为空，批次号：{" + messageBatch.getBatchId() + "}");
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }
                    final FeishuUser feishuUser = feishuService.getFeishuUser(messageBatch.getUserNum());
                    if (ObjectUtil.isNull(feishuUser)) {
                        // throw new BusinessException("推送消息失败！飞书用户不存在，批次号：{" + messageBatch.getBatchId() + "}");
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }

                    if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                        JSONObject resultByFile = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, feishuUser.getUserId(), FeishuService.MSG_TYPE_FILE, JSONUtil.createObj().set("file_key", fileKey));
                        if (resultByFile.get("code", Integer.class) != 0) {
                            // throw new BusinessException("推送EXCEL文件失败！批次号：{" + messageBatch.getBatchId() + "}");
                            messageBatch.setIsStatus("2");
                            messageBatch.setUpdatedTime(LocalDateTime.now());
                            umsContentService.updateById(messageBatch);
                            return;
                        }

                        logFeishuMsg(resultByFile, messageBatch);
                    }

                    JSONObject resultBySendMsg = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, feishuUser.getUserId(), FeishuService.MSG_TYPE_INTERACTIVE, cardJson);

                    if (resultBySendMsg.get("code", Integer.class) == 0) {
                        messageBatch.setIsStatus("1");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        logFeishuMsg(resultBySendMsg, messageBatch);
                    } else {
                        // throw new BusinessException("推送消息失败！批次号：{" + messageBatch.getBatchId() + "}");
                        messageBatch.setIsStatus("2");
                        messageBatch.setUpdatedTime(LocalDateTime.now());
                        umsContentService.updateById(messageBatch);
                        return;
                    }
                }

                if (!StrUtil.isBlank(messageBatch.getIsDaiban()) && messageBatch.getIsDaiban().equals("Y")) {
                    createTask(messageBatch.getBatchId());
                }

                //发送订阅信息
                sendSubscriptionNotification(notificationEntity, messageBatch, fileKey, cardJson);
                //发送订阅信息结束
            }
        }
    }

    @Async
    @Override
    public void sendSubscriptionNotification(NotificationEntity notificationEntity, UmsContent messageBatch, String fileKey, JSONObject cardJson) {
        try {
            List<String> subscriptionUserIds = notificationJobSubscriptionService.listSubscriptionUsers(notificationEntity.getPlanKey());
            if (subscriptionUserIds.size() > 0) {
                JSONObject sendSubMsgResult = feishuService.batchSendMessage(subscriptionUserIds, FeishuService.MSG_TYPE_INTERACTIVE, cardJson);

                if (sendSubMsgResult.get("code", Integer.class) == 0) {
                    log.info("推送订阅消息成功！");
                } else {
                    log.error("推送订阅消息失败！");
                }
                if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
                    for (String subscriptionUserId : subscriptionUserIds) {
                        JSONObject resultByFile = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, subscriptionUserId, FeishuService.MSG_TYPE_FILE, JSONUtil.createObj().set("file_key", fileKey));
                        if (resultByFile.get("code", Integer.class) != 0)
                            log.error("推送订阅EXCEL文件失败！批次号：{" + messageBatch.getBatchId() + "}");
                    }
                }

                if (!StrUtil.isBlank(messageBatch.getIsYunUrl())) {
                    JSONObject fileResult = feishuService.batchSendMessage(subscriptionUserIds,
                            FeishuService.MSG_TYPE_TEXT, JSONUtil.createObj().set("text", messageBatch.getIsYunUrl()));

                    if (fileResult.get("code", Integer.class) != 0)
                        log.error("推送订阅云文档URL失败！批次号：{" + messageBatch.getBatchId() + "}");
                }

//                            if (!StrUtil.isBlank(messageBatch.getSendFilePath())) {
//                                JSONObject batchSendFileResult = feishuService.batchSendMessage(subscriptionUserIds, FeishuService.MSG_TYPE_FILE, JSONUtil.createObj().set("file_key", fileKey));
//
//                                if (batchSendFileResult.get("code", Integer.class) != 0)
//                                    log.error("推送订阅消息文件失败！");
//                            }
            }
        } catch (Exception err) {
            log.error(err.getMessage());
        }
    }

    private void createTask(String batchId) {
        List<UmsContentSubDaiban> taskBatches = umsContentSubDaibanService.getUmsContentSubDaiban(batchId);

        if (taskBatches.size() == 0) {
            String msg = "当前没有需要推送任务，请检查！";
            log.error(msg);
            throw new BusinessException(msg);
        }

        for (UmsContentSubDaiban taskBatch : taskBatches) {
            JSONObject taskJson = umsContentSubDaibanService.getTaskJson(taskBatch);
            JSONObject resultByCreateTask = feishuService.createTask(FeishuService.RECEIVE_ID_TYPE_USER_ID, taskJson);
            if (resultByCreateTask.getInt("code") != 0) {
                taskBatch.setIsStatus("2");
                throw new BusinessException("创建任务失败！批次号：{" + batchId + "}");
            }

            String taskId = resultByCreateTask.getJSONObject("data").getJSONObject("task").getStr("id");
            taskBatch.setIsStatus("1");
            taskBatch.setFeishuTaskId(taskId);
            umsContentSubDaibanService.updateById(taskBatch);
        }
    }


    private void logFeishuMsg(JSONObject result, UmsContent umsContent) {
        JSONObject data = result.getJSONObject("data");
        String chatId = data.getStr("chat_id");
        String messageId = data.getStr("message_id");
        String msgType = data.getStr("msg_type");
        UmsContentFeishuMsgHistory umsContentFeishuMsgHistory = new UmsContentFeishuMsgHistory();
        umsContentFeishuMsgHistory.setChatId(chatId);
        umsContentFeishuMsgHistory.setMessageId(messageId);
        umsContentFeishuMsgHistory.setMsgType(msgType);
        umsContentFeishuMsgHistory.setBatchId(umsContent.getBatchId());
        umsContentFeishuMsgHistory.setConType(umsContent.getConType());
        umsContentFeishuMsgHistory.setConTypeDesc(umsContent.getConTypeDesc());
        umsContentFeishuMsgHistory.setMsgStatus(true);
        umsContentFeishuMsgHistoryService.add(umsContentFeishuMsgHistory);
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

                if (!StrUtil.isBlank(messageValue.getIsUrl()) && messageValue.getIsUrl().equals("Y")) {
                    if (StrUtil.isBlank(messageValue.getIsUrlName()))
                        markdownGroupMessage.addContent("[查看详情](" + msgContent + ")");
                    else
                        markdownGroupMessage.addContent("[" + messageValue.getIsUrlName() + "](" + msgContent + ")");
                } else {
                    markdownGroupMessage.addContent(msgContent);
                }
            }
            if (!StrUtil.isBlank(messageBatch.getLinkUrl())) {
                if (StrUtil.isBlank(messageBatch.getLinkUrlName()))
                    markdownGroupMessage.addContent("[查看详情](" + messageBatch.getLinkUrl() + ")");
                else
                    markdownGroupMessage.addContent("[" + messageBatch.getLinkUrlName() + "](" + messageBatch.getLinkUrl() + ")");
            }
            return markdownGroupMessage.toString();
        } else {
            return null;
        }
    }

    @Override
    public Result sendFeishuNotification(FeishuMessage feishuMessage) {
        if (feishuMessage.getSendType().equals(FeishuService.RECEIVE_ID_TYPE_USER_ID)) {
            FeishuUser feishuUser = feishuService.getFeishuUser(feishuMessage.getSendId());
            if (ObjectUtil.isNull(feishuUser)) return Result.fail("推送消息失败！飞书用户不存在");

            JSONObject cardJson = feishuApi.getMarkdownMessage(feishuMessage.getContent(), null);

            JSONObject resultBySendMsg = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, feishuUser.getUserId(), FeishuService.MSG_TYPE_INTERACTIVE, cardJson);

            if (resultBySendMsg.get("code", Integer.class) == 0) return Result.success();
            else return Result.fail("推送消息失败！");
        } else if (feishuMessage.getSendType().equals(FeishuService.RECEIVE_ID_TYPE_CHAT_ID)) {
            JSONObject cardJson = feishuApi.getMarkdownMessage(feishuMessage.getContent(), null);
            String chatId = feishuService.fetchChatIdByRobot(feishuMessage.getSendId());
            if (StrUtil.isBlank(chatId)) return Result.fail("推送消息失败，群不存在或者未将机器人拉进群聊！");
            JSONObject resultBySendMsg = feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID, chatId, FeishuService.MSG_TYPE_INTERACTIVE, cardJson);
            if (resultBySendMsg.get("code", Integer.class) == 0) return Result.success();
            else return Result.fail("推送消息失败！");
        } else {
            return Result.fail("未知推送类型，请检查！");
        }
    }
}
