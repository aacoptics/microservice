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
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldType;
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
    private String competitorName;
    private String competitorLink;
    private String assemblyDrawing;
}
