package com.aacoptics.wlg.dashboard.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_wlg_molding_machine_param_data")
public class MoldingMachineParamData implements Serializable {

    private String machineName;

    private String waferId;

    private String recipeName;

    private String recipePhase;

    private String paramName;

    private String paramValue;

    private LocalDateTime plcTime;

    private LocalDateTime createTime;

    private Long plcTimeStamp;
}