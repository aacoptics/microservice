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
@TableName("t_structure_data")
public class StructureData extends BasePo {
    private String department; //事业部
    private String lensNumber; //镜片数
    private String category;
    private String project;
    private String partName;
    private String material;
    private String structureSchemesNo; // 结构方案总数
    private String coreThicknessLens;
    private String maxWallThickness;
    private String minWallThickness;
    private String maxCoreRatio;
    private String maxMinRatio;
    private String opticsMaxAngleR1; //最大光学角度R1
    private String opticsMaxAngleR2; //最大光学角度R2
    private String outerDiameter;
    private String edgeThickness;
    private String wholeMinWallThickness;
    private String wholeMaxWallThickness;
    private String wholeMaxMinRatio;
    private String wholeDiameterThicknessRatio;
    private String maxAngleR1;
    private String maxAngleR2;
    private String r1MaxHeightDifference;
    private String r2MaxHeightDifference;
    private String r1R2Distance;
    private String middlePartThickness;
    private String bottomDiameterDistance;
    private String mechanismDiameterThicknessRatio;
    private String r1KanheAngle;
    private String r1KanheHeight;
    private String r2KanheAngle;
    private String r2KanheHeight;
    private String r1Srtm;
    private String r2Srtm;
    private String r1SplitPosition; //R1分割位位置
    private String r2SplitPosition; //R2分割位位置
    private String outerDiameterSrtm;
    private String partSurfaceLiftRatio; //分型面上抬比例
    private String mechanismTrou; //机构逃肉
    private String assemblyDrawing;
}
