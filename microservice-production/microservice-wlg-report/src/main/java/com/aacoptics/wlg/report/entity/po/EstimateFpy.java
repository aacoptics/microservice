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
@TableName("estimate_fpy")
public class EstimateFpy extends BasePo {

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
     * 预估直通率日期
     */
    private LocalDate fpyDate;

    /**
     * 直通率
     */
    private BigDecimal fpy;


    /**
     * WLG预估结存
     */
    private BigDecimal estimateBalance;
}