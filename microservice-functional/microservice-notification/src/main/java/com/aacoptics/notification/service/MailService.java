package com.aacoptics.notification.service;


import com.aacoptics.notification.entity.form.EmailSendForm;
import com.aacoptics.notification.entity.po.EmailSend;

import javax.mail.MessagingException;

public interface MailService {
    boolean send(EmailSend emailSend) throws MessagingException;

    boolean send(EmailSendForm emailSendForm);
}
