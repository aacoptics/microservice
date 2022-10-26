package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineBudgetQueryParam extends BaseParam<ProductLineBudget> {

    /**
     * 上传日志ID
     */
    private Long uploadLogId;

    /**
     * 事业部
     */
    private String businessDivision;

    /**
     * 产品线
     */
    private List<String> productLineList;

}
