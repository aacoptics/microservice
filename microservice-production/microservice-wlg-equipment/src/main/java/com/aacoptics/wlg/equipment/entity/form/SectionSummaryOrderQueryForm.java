package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseQueryForm;
import com.aacoptics.wlg.equipment.entity.param.SectionSummaryOrderQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel
@Data
public class SectionSummaryOrderQueryForm extends BaseQueryForm<SectionSummaryOrderQueryParam> {

    private String sectionType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createDateEnd;
}
