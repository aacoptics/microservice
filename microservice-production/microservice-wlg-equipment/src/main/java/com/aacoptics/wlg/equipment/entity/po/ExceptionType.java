package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_exception_type")
public class ExceptionType extends BasePo {

    /**
     * 异常分类
     */
    @TableField(value = "exception_type")
    private String exceptionType;

    /**
     * 异常子类
     */
    @TableField(exist = false)
    private List<ExceptionSubClass> exceptionSubClassList;


}
