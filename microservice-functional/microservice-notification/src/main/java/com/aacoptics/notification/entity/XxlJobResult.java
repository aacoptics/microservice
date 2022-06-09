package com.aacoptics.notification.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class XxlJobResult implements Serializable {

    private Integer code;

    private Object content;

    private String msg;


}
