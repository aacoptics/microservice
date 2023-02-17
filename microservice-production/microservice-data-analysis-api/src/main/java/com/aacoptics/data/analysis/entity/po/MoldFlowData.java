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
@TableName("t_mold_flow")
public class MoldFlowData extends BasePo {
    private String department; //事业部
    private String lensNumber; //镜片数
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldType;
    private String runnerType; //流道类型
    private String moldDiameterRate;
    private String flowFrontTemperature;
    private String vpChangePressure;
    private String simulateWireLength;
    private String wholePercent;
    private String effectiveR1;
    private String effectiveR2;
    private String ridgeR1;
    private String ridgeR2;
    private String refractiveR1;
    private String refractiveR2;
    private String refractivePicR1; //模拟面型图R1
    private String refractivePicR2; //模拟面型图R2
    private String preFrontR1; // 预补正面型R1
    private String preFrontR2; // 预补正面型R2
    private String competitorName;
    private String competitorLink;
    private String assemblyDrawing;
}
