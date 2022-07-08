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
@TableName("production_summary")
public class ProductionSummary extends BasePo {

    /**
     * 项目
     */
    private String projectName;

    /**
     * 生产月份
     */
    private LocalDate productionDate;


    /**
     * 目标生产数量
     */
    private BigDecimal targetQty;


    /**
     * 实际生产数量
     */
    private BigDecimal actualQty;


}