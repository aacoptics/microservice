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
    private String department; //事业部
    private String lensNumber; //镜片数
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo;
    private String moldType; //模具类型
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
    private String wftR1Pic; //白片面R1图片
    private String wftR2Pic; //白片面R2图片
    private String wftStability;
    private String wftConsistency;
    private String wftMaxAs;
    private String cftR1;
    private String cftR2;
    private String cftR1Pic; //镀膜片R1图片
    private String cftR2Pic; //镀膜片R2图片
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
    private String abcFilesNo; //ABC档合格数分布
    private String structureNo; //结构方案总数
    private String moldTypeNo; //模具类型总数
    private String moldCost; //模具费用
    private String evtTime; //项目EVT耗时
    private String dvtTime; //项目DVT耗时
    private String evtDvtTime; //项目(EVT+DVT)耗时
    private String evtCost; //项目EVT费用
    private String dvtCost; //项目DVT费用
    private String evtDvtCost; //项目(EVT+DVT)费用
    private String projectMassProduction; //项目量产
    private String mtfAvgYield;// MTF平均良率
    private String massProductionTime;// 量产时长（首次）
    private String massProductionShipment;// 量产出货量（首次）
    private String projectInitiationTime;//项目立项时间
}
