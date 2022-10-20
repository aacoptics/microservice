package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineBudgetQueryParam extends BaseParam<ProductLineBudget> {

    /**
     * 上传日志ID
     */
    private Long uploadLogId;

}
