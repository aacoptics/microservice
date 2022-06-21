package com.aacoptics.gaia.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("Z_Class_by_GY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GaiaClass implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Class_ID")
    private String classID;

    @TableField("Class_des")
    private String classDes;

    @TableField("state")
    private String state;

    @TableField("creater")
    private String creator;

    @TableField("Createtime")
    private LocalDateTime createTime;

    @TableField("Updatetime")
    private LocalDateTime updateTime;

    @TableField("Modify")
    private String modify;
}
