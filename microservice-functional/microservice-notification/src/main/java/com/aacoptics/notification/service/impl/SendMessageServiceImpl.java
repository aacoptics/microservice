package com.aacoptics.notification.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.entity.po.UmsContent;
import com.aacoptics.notification.entity.po.UmsContentSub;
import com.aacoptics.notification.entity.vo.MarkdownGroupMessage;
import com.aacoptics.notification.entity.vo.MessageTypeInfo;
import com.aacoptics.notification.entity.vo.NotificationEntity;
import com.aacoptics.notification.provider.DingTalkApi;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.service.RobotService;
import com.aacoptics.notification.service.SendMessageService;
import com.aacoptics.notification.service.UmsContentService;
import com.aacoptics.notification.service.UmsContentSubService;
import com.aacoptics.notification.utils.DingTalkUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public void sendHandledMessage(NotificationEntity notificationEntity) throws Exception {
        List<UmsContent> messageBatches;
        if (StrUtil.isBlank(notificationEntity.getBatchId())) {
            messageBatches = umsContentService.getUmsContent(notificationEntity.getPlanKey());
        } else {
            messageBatches = umsContentService.getUmsContentByBatchId(notificationEntity.getBatchId());
        }
        if (messageBatches.size() <= 0) {
            String msg = "当前没有需要推送的批次号！";
            log.error(msg);
            throw new Exception(msg);
        }
        for (UmsContent messageBatch : messageBatches) {
            String markdownGroupMessage = getMarkDownMessage(messageBatch);
            if (markdownGroupMessage == null) {
                String msg = "拼接消息失败，请检查！";
                log.error(msg);
                throw new Exception(msg);
            }

            if (notificationEntity.getMsgTypeInfo() == null || notificationEntity.getMsgTypeInfo().size() <= 0) {
                String msg = "未配置消息类型，请检查！";
                log.error(msg);
                throw new Exception(msg);
            }
            List<String> robotNames = notificationEntity.getMsgTypeInfo().stream().map(Robot::getRobotName).collect(Collectors.toList());
            List<Robot> robotList = robotService.findByName(robotNames);
            for (Robot messageTypeInfo : robotList) {
                if (messageTypeInfo.getRobotType().equals("FeiShu")) {
                    String message = feishuApi.SendGroupMessage(messageTypeInfo.getRobotUrl(), markdownGroupMessage);
                    new JSONObject();
                    JSONObject messageJson;
                    try {
                        messageJson = JSONObject.parseObject(message);
                    } catch (Exception err) {
                        String msg = "解析返回值失败！{" + err.getMessage() + "}";
                        log.error(msg);
                        throw new Exception(msg);
                    }
                    if (messageJson.containsKey("StatusCode") && messageJson.getInteger("StatusCode") == 0) {
                        messageBatch.setIsStatus("1");
                        umsContentService.updateById(messageBatch);
                    } else {
                        String errorMsg;
                        if (messageJson.containsKey("msg") && !StringUtils.isEmpty(messageJson.getString("msg"))) {
                            errorMsg = messageJson.getString("msg");
                            log.error(errorMsg);
                            throw new Exception(errorMsg);
                        }
                    }
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