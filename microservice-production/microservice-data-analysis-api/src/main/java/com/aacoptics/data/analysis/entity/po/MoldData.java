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
    private String category;
    private String project;
    private String partName;
    private String material;
    private String moldType;
    private String moldCorePassivation;
    private String runnerType;
    private String firstRunner;
    private String secondRunner;
    private String thirdRunner;
    private String partingSurface;
    private String splitPosition;
    private String gateType;
    private String gateWidth;
    private String gateThickness;
    private String gateR1Thickness;
    private String gateR2Thickness;
    private String moldOpeningType;
}
