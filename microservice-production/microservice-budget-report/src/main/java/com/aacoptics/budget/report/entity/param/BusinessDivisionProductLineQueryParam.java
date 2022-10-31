package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.BusinessDivisionProductLine;
import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDivisionProductLineQueryParam extends BaseParam<BusinessDivisionProductLine> {


    /**
     * 事业部
     */
    private String businessDivision;

    /**
     * 产品线
     */
    private String productLine;


}
