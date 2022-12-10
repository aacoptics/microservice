package com.aacoptics.wlg.equipment.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionSummaryOrderQueryParam extends BaseParam {

    private String sectionType;

    private LocalDateTime createDateStart;

    private LocalDateTime createDateEnd;
}
