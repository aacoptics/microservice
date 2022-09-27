package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.config.BusConfig;
import com.aacoptics.notification.entity.form.EmailSendForm;
import com.aacoptics.notification.entity.po.EmailSend;
import com.aacoptics.notification.service.MailService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RefreshScope
public class MailServiceImpl implements MailService {
    @Resource
    JavaMailSender mailSender;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Value("${spring.mail.from}")
    private String from;

    @Override
    public boolean send(EmailSend emailSend) throws MessagingException {
        if(StringUtils.isBlank(emailSend.getEmailContent()))
            return false;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(emailSend.getTo().toArray(new String[emailSend.getTo().size()]));
        helper.setSubject(emailSend.getSubject());
        helper.setText(emailSend.getEmailContent(), true);
        mailSender.send(message);
        return true;
    }

    @Override
    public boolean send(EmailSendForm emailSendForm) {
        try {
            String messageId = String.valueOf(UUID.randomUUID());
            String messageData = JSONObject.toJSONString(emailSendForm.toPo(EmailSend.class));
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> emailMap = new HashMap<>();
            emailMap.put("messageId", messageId);
            emailMap.put("messageData", messageData);
            emailMap.put("createTime", createTime);
            rabbitTemplate.convertAndSend(BusConfig.EXCHANGE_NAME,
                    BusConfig.EMAIL_ROUTING,
                    emailMap);
            return true;
        }
        catch(Exception err){
            log.error("发送消息失败，请检查！");
            return false;
        }
    }
}