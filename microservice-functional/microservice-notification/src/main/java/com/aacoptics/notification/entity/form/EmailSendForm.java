package com.aacoptics.notification.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.notification.entity.po.EmailSend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Slf4j
public class EmailSendForm extends BaseForm<EmailSend> {

    @NotEmpty(message = "收件人列表不能为空")
    @ApiModelProperty(value = "收件人列表")
    private List<String> to = new ArrayList<>();

    @NotBlank(message = "主题不能为空")
    @ApiModelProperty(value = "主题")
    private String subject;

    @ApiModelProperty(value = "邮件内容")
    private String emailContent;

    @Override
    public EmailSend toPo(Class<EmailSend> clazz) {
        EmailSend emailSend = new EmailSend();
        BeanUtils.copyProperties(this, emailSend);
        return emailSend;
    }
}
