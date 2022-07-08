package com.aacoptics.wlg.report.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionSummaryQueryParam extends BaseParam<ProductionSummary> {
    private String projectName;

    private LocalDate productionDateStart;

    private LocalDate productionDateEnd;
}
