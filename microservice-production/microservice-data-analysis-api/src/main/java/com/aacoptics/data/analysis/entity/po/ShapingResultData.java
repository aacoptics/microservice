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
@TableName("t_shaping_result_data")
public class ShapingResultData extends BasePo {
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo;
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
}
