package com.aacoptics.notification.entity.po;

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
@TableName("ums_01_content")
public class UmsContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String batchId;

    private String conType;

    private String conTypeDesc;

    private String isStatus;

    private String linkUrl;

    private String sendType;

    private String userNum;

    private String sendFilePath;

    private String sendPicturePath;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;
}