package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.ProductionCostBudget;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionCostBudgetQueryParam extends BaseParam<ProductionCostBudget> {

    /**
     * 上传日志ID
     */
    private Long uploadLogId;

}
