package com.aacoptics.wlg.equipment.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.equipment.entity.po.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionTypeQueryParam extends BaseParam<ExceptionType> {


    /**
     * 主键
     */
    private Long id;


    /**
     * 异常分类
     */
    private String exceptionType;



}
