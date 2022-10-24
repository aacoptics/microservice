package com.aacoptics.budget.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("fb_production_cost_budget")
public class ProductionCostBudget extends BasePo {

    /**
     * 事业部
     */
    @TableField(value = "business_division")
    private String businessDivision;


    /**
     * 产品线
     */
    @TableField(value = "product_line")
    private String productLine;

    /**
     * 数据版本
     */
    @TableField(value = "data_version")
    private String dataVersion;


    /**
     * 科目序号
     */
    @TableField(value = "item_seq")
    private Integer itemSeq;


    /**
     * 分类1
     */
    @TableField(value = "category_1")
    private String category1;

    /**
     * 分类2
     */
    @TableField(value = "category_2")
    private String category2;


    /**
     * 分类3
     */
    @TableField(value = "category_3")
    private String category3;


    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;


    /**
     * 校验
     */
    @TableField(value = "validation")
    private String validation;


    /**
     * 年份
     */
    @TableField(value = "year")
    private Integer year;



    /**
     * 月份-01
     */
    @TableField(value = "month_01_value")
    private BigDecimal month01Value;

    /**
     * 月份-02
     */
    @TableField(value = "month_02_value")
    private BigDecimal month02Value;

    /**
     * 月份-03
     */
    @TableField(value = "month_03_value")
    private BigDecimal month03Value;

    /**
     * 月份-04
     */
    @TableField(value = "month_04_value")
    private BigDecimal month04Value;

    /**
     * 月份-05
     */
    @TableField(value = "month_05_value")
    private BigDecimal month05Value;

    /**
     * 月份-06
     */
    @TableField(value = "month_06_value")
    private BigDecimal month06Value;

    /**
     * 月份-07
     */
    @TableField(value = "month_07_value")
    private BigDecimal month07Value;

    /**
     * 月份-08
     */
    @TableField(value = "month_08_value")
    private BigDecimal month08Value;

    /**
     * 月份-09
     */
    @TableField(value = "month_09_value")
    private BigDecimal month09Value;

    /**
     * 月份-10
     */
    @TableField(value = "month_10_value")
    private BigDecimal month10Value;

    /**
     * 月份-11
     */
    @TableField(value = "month_11_value")
    private BigDecimal month11Value;

    /**
     * 月份-12
     */
    @TableField(value = "month_12_value")
    private BigDecimal month12Value;

    /**
     * YTD
     */
    @TableField(value = "ytd_value")
    private BigDecimal ytdValue;

    /**
     * YTG
     */
    @TableField(value = "ytg_value")
    private BigDecimal ytGValue;

    /**
     * 年度预算
     */
    @TableField(value = "year_value")
    private BigDecimal yearValue;

    /**
     * 上传日志ID
     */
    private Long uploadLogId;

}
