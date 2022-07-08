package com.aacoptics.wlg.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("production_plan")
public class ProductionPlan extends BasePo {

    /**
     * 项目
     */
    private String projectName;

    /**
     * 模具
     */
    private String mold;

    /**
     * 周期
     */
    private String cycle;

    /**
     * 条件代码
     */
    private String code;


    /**
     * 名称
     */
    private String name;

    /**
     * 计划日期
     */
    private LocalDate planDate;

    /**
     * 计划值
     */
    private BigDecimal planValue;

}