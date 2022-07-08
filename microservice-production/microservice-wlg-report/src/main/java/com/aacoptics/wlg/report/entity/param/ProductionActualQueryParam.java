package com.aacoptics.wlg.report.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.report.entity.po.ProductionActual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionActualQueryParam extends BaseParam<ProductionActual> {

    private String projectName;

    private String product;

    private String mold;

    private String cycle;

    private LocalDate actualDateStart;

    private LocalDate actualDateEnd;
}
