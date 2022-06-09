package com.aacoptics.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Shao Xiang
 * @since 2022-03-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_01_content_sub")
public class UmsContentSub implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String batchId;

    private String seqNo;

    private String valueDesc;

    private String isBold;

    private String isDeleteline;

    private String isItalics;

    private String isUnderline;

    private String isSeqno;

    private String isList;

    private String isColour;

    private String isTypeface;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;

}
