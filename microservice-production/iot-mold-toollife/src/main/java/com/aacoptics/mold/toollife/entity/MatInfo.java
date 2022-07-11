package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("INV_TOOL_SPLIT_DETAIL")
public class MatInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("handle_code")
    private String handleCode;

    @TableField("tool_code")
    private String toolCode;

    @TableField("mat_code")
    private String matCode;

    @TableField("mat_name")
    private String matName;

    @TableField("life_salvage")
    private String lifeSalvage;

    @TableField(exist = false)
    private String actualLife;

    @TableField(exist = false)
    private String leftLife;
}