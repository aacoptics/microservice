package com.aacoptics.temphumidity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Shao Xiang
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class FeishuMessage implements Serializable {

    private String content;

    private String sendId;

    private String sendType;
}
