package com.aacoptics.notification.entity.vo;

import com.aacoptics.notification.entity.po.Robot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
public class DingTalkMessage implements Serializable {

    private String title;

    private String content;

    private String userIdList;
}
