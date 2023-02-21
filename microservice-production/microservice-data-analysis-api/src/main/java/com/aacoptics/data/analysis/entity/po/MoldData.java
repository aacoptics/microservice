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
@TableName("t_mold_data")
public class MoldData extends BasePo {
    private String department; // 事业部
    private String lensNumber; // 镜片数
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldNo; // 模具序号
    private String moldType;
    private String moldTypeTotal;
    private String moldCorePassivation;
    private String runnerType;
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
