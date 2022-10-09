package com.aacoptics.data.analysis.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_process_condition_data")
public class ProcessConditionData extends BasePo {
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo;
    private String moldTemp;
    private String materialTemp;
    private String jetVelocity;
    private String vpSwitch;
    private String holdPressure1;
    private String holdPressure2;
    private String holdPressure3;
    private String holdPressure4;
    private String holdPressure5;
    private String holdPressure6;
    private String holdTime1;
    private String holdTime2;
    private String holdTime3;
    private String holdTime4;
    private String holdTime5;
    private String holdTime6;
    private String holdPressureVelocity;
    private String platenPosition;
    private String openingSpeed;
    private String ejectionSpeed;
    private String coolingTime;
    private String clampingForce;
    private String passivation;
}
