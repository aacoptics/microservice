package com.aacoptics.wlg.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("target_delivery")
public class TargetDelivery extends BasePo {

    /**
     * 物料号
     */
    private String itemNumber;


    /**
     * 物料描述
     */
    private String itemDescription;


    /**
     * 项目
     */
    private String projectName;

    /**
     * 交货日期
     */
    private LocalDate deliveryDate;

    /**
     * 交货数量
     */
    private Long deliveryQty;

}