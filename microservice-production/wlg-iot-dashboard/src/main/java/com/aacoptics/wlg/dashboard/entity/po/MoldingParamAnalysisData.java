package com.aacoptics.wlg.dashboard.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_wlg_molding_machine_param_analysis_data")
public class MoldingParamAnalysisData implements Serializable {

    private Integer id;
    private String machineName;
    private String waferId;
    private String recipePhase;
    private String recipeName;
    private String paramName;
    private Double avgValue;
    private Double stdValue;
    private LocalDateTime createTime;
}