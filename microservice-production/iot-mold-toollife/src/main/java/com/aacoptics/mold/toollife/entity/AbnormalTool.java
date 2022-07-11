package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("tb_mold_abnormal_tool_list")
public class AbnormalTool implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String toolNo;

    private String matCode;

    private String matName;

    private String lifeSalvage;

    private LocalDateTime scrapedTime;

    private String realLifeSalvage;

    private String area;

    private String lastMachineNo;

    private String reason;

    private String checkedPerson;

    private LocalDateTime checkedTime;

    private Boolean isConfirmed;

    private String confirmedPerson;

    private LocalDateTime confirmedTime;

    private LocalDateTime createDateTime;

    private Integer abnormalType;

    @TableField(exist = false)
    private String lifeRate;


}