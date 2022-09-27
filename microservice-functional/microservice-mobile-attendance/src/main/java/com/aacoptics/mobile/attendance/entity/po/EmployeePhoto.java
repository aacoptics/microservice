package com.aacoptics.mobile.attendance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_Photo")
public class EmployeePhoto implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Code")
    private String code;

    @TableField("Photo")
    private byte[] photo;

    @TableField("CreateTime")
    private LocalDateTime createTime;

    @TableField("IsUpload")
    private Integer isUpload;
}