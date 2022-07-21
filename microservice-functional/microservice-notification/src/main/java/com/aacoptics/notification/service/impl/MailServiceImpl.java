package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.EmailSend;
import com.aacoptics.notification.service.MailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    @Resource
    JavaMailSender mailSender;

    //@Value(value = "${spring.profiles}")
    private String from = "s-optics@aacoptics.com";

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
}