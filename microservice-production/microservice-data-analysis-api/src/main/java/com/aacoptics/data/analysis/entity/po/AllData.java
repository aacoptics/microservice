package com.aacoptics.data.analysis.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllData {
    private String department;
    private String lensNumber;
    private String category;
    private String project;
    private String partName;
    private String material;

    // 工艺条件数据
    private String pcMoldNo;
    private String pcMoldType;
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
    private String mfCoolingTime;
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
    private String holdPressureVelocity;
    private String platenPosition;
    private String openingSpeed;
    private String ejectionSpeed;
    private String coolingTime;
    private String clampingForce;
    private String passivation;

    // 成型结果数据
    private String srMoldNo;
    private String srMoldType;
    private String coreThickness;
    private String coreThicknessRange;
    private String r1VectorHeight;
    private String r1VectorHeightRange;
    private String r2VectorHeight;
    private String r2VectorHeightRange;
    private String outerDiameterEcc;
    private String kanheEcc;
    private String faceEcc;
    private String annealingProcess;
    private String bpKanheRoundness;
    private String dmpKanheRoundness;
    private String outerDiameterAverage;
    private String outerDiameterRange;
    private String outerDiameterRoundness;
    private String outerDiameterShrinkage;
    private String outerDiameterRoughness;
    private String r1Flatness;
    private String r2Flatness;
    private String r1SplitAverage;
    private String r2SplitAverage;
    private String wftR1;
    private String wftR2;
    private String wftR1Pic;
    private String wftR2Pic;
    private String wftStability;
    private String wftConsistency;
    private String wftMaxAs;
    private String cftR1;
    private String cftR2;
    private String cftR1Pic;
    private String cftR2Pic;
    private String cftConsistency;
    private String cftMaxAs;
    private String coatingTrend;
    private String cfsrR1;
    private String cfsrR2;
    private String cfsrR1R2;
    private String burr;
    private String weldline;
    private String appearanceProblem;
    private String appearanceImg;
    private String remarks;
    private String abcFilesNo;
    private String structureNo;
    private String moldTypeNo;
    private String moldCost;
    private String evtTime;
    private String dvtTime;
    private String evtDvtTime;
    private String evtCost;
    private String dvtCost;
    private String evtDvtCost;
    private String projectMassProduction;

    // 结构数据
    private String coreThicknessLens;
    private String maxWallThickness;
    private String minWallThickness;
    private String maxCoreRatio;
    private String maxMinRatio;
    private String opticsMaxAngleR1;
    private String opticsMaxAngleR2;
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
    private String r1SplitPosition;
    private String r2SplitPosition;
    private String outerDiameterSrtm;
    private String partSurfaceLiftRatio;
    private String mechanismTrou;
    private String assemblyDrawing;

    // 模流数据
    private String mfMoldType;
    private String mfRunnerType;
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
    private String refractivePicR1;
    private String refractivePicR2;
    private String competitorName;
    private String competitorLink;
    private String competitorAssemblyDrawing;// 竞品组立图

    // 模具数据
    private String mdMoldNo;
    private String mdMoldType;
    private String moldCorePassivation;
    private String mdRunnerType;
    private String cavityInnerDiameter; // 型腔内径
    private String cavityInnerDiameterRange; // 型腔内径极差
    private String firstRunner;
    private String secondRunner;
    private String thirdRunner;
    private String partingSurface;
    private String splitPositionR1;// R1分割位排气
    private String splitPositionR2;// R2分割位排气
    private String gateType;
    private String gateWidth;
    private String gateThickness;
    private String gateR1Thickness;
    private String gateR2Thickness;
    private String moldOpeningType;

}
