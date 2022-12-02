package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_exception_sub_class")
public class ExceptionSubClass extends BasePo {

    /**
     * 主表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "exception_type_id")
    private Long exceptionTypeId;


    /**
     * 异常子类
     */
    @TableField(value = "sub_class")
    private String subClass;

}
