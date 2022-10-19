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
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo;

    private String mfMoldTemp;
    private String mfMaterialTemp;
    private String mfJetVelocity;
    private String mfVpSwitch;
    private String mfHoldPressure1;
    private String mfHoldPressure2;
    private String mfHoldPressure3;
    private String mfHoldPressure4;
    private String mfHoldPressure5;
    private String mfHoldPressure6;
    private String mfHoldTime1;
    private String mfHoldTime2;
    private String mfHoldTime3;
    private String mfHoldTime4;
    private String mfHoldTime5;
    private String mfHoldTime6;

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

    private String wftStability;
    private String wftConsistency;
    private String wftMaxAs;

    private String cftR1;
    private String cftR2;
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

    private String coreThicknessLens;
    private String maxWallThickness;
    private String minWallThickness;
    private String maxCoreRatio;
    private String maxMinRatio;
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
    private String outerDiameterSrtm;
    private String assemblyDrawing;

}
