package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetUploadLogQueryParam extends BaseParam<BudgetUploadLog> {

    /**
     * 类型
     */
    private String type;

    /**
     * Excel名称
     */
    private String excelName;



}
