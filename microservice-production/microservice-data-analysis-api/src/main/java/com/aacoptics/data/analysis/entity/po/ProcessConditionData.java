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
    private String department; //事业部
    private String lensNumber; // 镜片数
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo;
    private String moldType; //模具类型
    private String mfMoldTemp;
    private String mfMaterialTemp;
    private String mfJetVelocity;
    private String mfVpSwitch;
    private String mfHoldPressure1;
    private String mfHoldTime1;
    private String mfHoldPressure2;
    private String mfHoldTime2;
    private String mfHoldPressure3;
    private String mfHoldTime3;
    private String mfHoldPressure4;
    private String mfHoldTime4;
    private String mfHoldPressure5;
    private String mfHoldTime5;
    private String mfHoldPressure6;
    private String mfHoldTime6;
    private String mfCoolingTime; //模流冷却时间
    private String moldTemp;
    private String materialTemp;
    private String jetVelocity;
    private String vpSwitch;
    private String holdPressure1;
    private String holdTime1;
    private String holdPressure2;
    private String holdTime2;
    private String holdPressure3;
    private String holdTime3;
    private String holdPressure4;
    private String holdTime4;
    private String holdPressure5;
    private String holdTime5;
    private String holdPressure6;
    private String holdTime6;
    // private String holdPressureVelocity; // 保压速度
    private String platenPosition;
    private String openingSpeed;
    private String ejectionSpeed;
    private String coolingTime;
    private String clampingForce;
    private String passivation;
}
