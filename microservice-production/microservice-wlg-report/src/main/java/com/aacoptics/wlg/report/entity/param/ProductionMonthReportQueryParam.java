package com.aacoptics.wlg.report.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.report.entity.po.ProductionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionMonthReportQueryParam extends BaseParam<ProductionPlan> {

    private String projectName;

    private LocalDate dateStart;

    private LocalDate dateEnd;
}
