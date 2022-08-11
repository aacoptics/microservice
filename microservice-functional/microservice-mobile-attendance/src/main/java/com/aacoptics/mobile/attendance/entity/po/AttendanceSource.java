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
@TableName("T_Source_Feishu_test")
public class AttendanceSource implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField(value = "Code")
    private String code;

    @TableField(value = "CardNo")
    private String cardNo;

    @TableField(value = "MachID")
    private Integer machId;

    @TableField(value = "FDateTime")
    private LocalDateTime fDateTime;

    @TableField(value = "CreateTime")
    private LocalDateTime createTime;

    @TableField(value = "UpdateTime")
    private LocalDateTime updateTime;

    @TableField(value = "IsDeal")
    private boolean isDeal;
}