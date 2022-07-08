package com.aacoptics.wlg.report.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequirementQueryParam extends BaseParam<CustomerRequirement> {
    private String projectName;

    private LocalDate requirementDateStart;

    private LocalDate requirementDateEnd;
}
