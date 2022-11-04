package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLinePermissionQueryParam extends BaseParam<ProductLinePermission> {

    /**
     * 类型
     */
    private String type;

    /**
     * 事业部
     */
    private String businessDivision;

    /**
     * 产品线
     */
    private String productLine;

    /**
     * 域账号
     */
    private String userCode;

}
