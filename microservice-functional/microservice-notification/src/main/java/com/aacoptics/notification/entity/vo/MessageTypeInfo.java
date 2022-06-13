package com.aacoptics.notification.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MessageTypeInfo  implements Serializable {

    private String msgType;

    private String robotUrl;
}
