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
public class ProductionProjectReportQueryParam extends BaseParam<ProductionPlan> {

    private String mold;

    private String cycle;

    private String projectName;

    private LocalDate dateStart;

    private LocalDate dateEnd;
}
