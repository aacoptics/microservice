package com.aacoptics.notification.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class FeishuMessage implements Serializable {

    private String content;

    private String jobNumber;
}
