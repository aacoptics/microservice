package com.aacoptics.notification.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.MarkdownGroupMessage;
import com.aacoptics.notification.entity.UmsContent;
import com.aacoptics.notification.entity.UmsContentSub;
import com.aacoptics.notification.provider.FeishuApi;
import com.aacoptics.notification.service.SendMessageService;
import com.aacoptics.notification.service.UmsContentService;
import com.aacoptics.notification.service.UmsContentSubService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    @Resource
    UmsContentService umsContentService;

    @Resource
    UmsContentSubService umsContentSubService;

    @Resource
    FeishuApi feishuApi;

    @Override
    public void sendFeishuMessage(String robotWebhook) {
        List<UmsContent> messageBatches = umsContentService.getUmsContent();

        if (messageBatches.size() > 0) {
            for (UmsContent messageBatch : messageBatches) {
                String markdownGroupMessage = getMarkDownMessage(messageBatch);
                if (markdownGroupMessage != null) {

                    String message = feishuApi.SendGroupMessage(robotWebhook, markdownGroupMessage);
                    JSONObject messageJson = new JSONObject();
                    try {
                        messageJson = JSONObject.parseObject(message);
                    } catch (Exception err) {
                        log.error("解析返回值失败！{}", err.getMessage());
                    }
                    if (messageJson.containsKey("StatusCode") && messageJson.getInteger("StatusCode") == 0) {
                    } else {
                        String errorMsg = null;
                        if (messageJson.containsKey("msg") && !StringUtils.isEmpty(messageJson.getString("msg"))) {
                            errorMsg = messageJson.getString("msg");
                        }
                    }
                }
            }
        }
    }

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

                if (!StrUtil.isBlank(messageValue.getIsBold()) && messageValue.getIsBold().equals("Y")) {
                    markdownGroupMessage.addBlobContent(messageValue.getValueDesc());
                } else {
                    markdownGroupMessage.addContent(messageValue.getValueDesc());
                }
            }
            if (!StrUtil.isBlank(messageBatch.getLinkUrl())) {
                markdownGroupMessage.addContent("[查看详情](" + messageBatch.getLinkUrl() + ")");
            }
            return markdownGroupMessage.toString();
        } else {
            return null;
        }
    }
}