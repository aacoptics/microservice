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
@TableName("production_actual")
public class ProductionActual extends BasePo {

    /**
     * 项目
     */
    private String projectName;

    /**
     * 产品
     */
    private String product;

    /**
     * 模具
     */
    private String mold;

    /**
     * 周期
     */
    private String cycle;

    /**
     * 生产日期
     */
    private LocalDate actualDate;

    /**
     * 实际预估收穴数
     */
    private Long estimateHoleQty;

    /**
     * 实际模压投入片数（PCS)
     */
    private Long moldPressInputQty;

    /**
     * 实际模压产出片数(PCS)
     */
    private Long moldPressOutputQty;

    /**
     * 实际后道领料(PCS)
     */
    private Long afterAcquisitionQty;

    /**
     * 实际性能良率
     */
    private BigDecimal performanceYield;

    /**
     * 后道实际投料(PCS)
     */
    private Long afterInputQty;

    /**
     * 实际后道产出（颗)
     */
    private Long afterOutputQty;

    /**
     * 实际后道良率
     */
    private BigDecimal afterYield;

    /**
     * 实际入库（转镀膜）
     */
    private Long inventoryQty;

}