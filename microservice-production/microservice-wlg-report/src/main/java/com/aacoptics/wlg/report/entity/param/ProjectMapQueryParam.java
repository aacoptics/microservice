package com.aacoptics.wlg.report.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.wlg.report.entity.po.MoldUse;
import com.aacoptics.wlg.report.entity.po.ProjectMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMapQueryParam extends BaseParam<ProjectMap> {

    /**
     * 商务项目号
     */
    private String businessProject;

    /**
     * 内部项目
     */
    private String internalProject;

}
