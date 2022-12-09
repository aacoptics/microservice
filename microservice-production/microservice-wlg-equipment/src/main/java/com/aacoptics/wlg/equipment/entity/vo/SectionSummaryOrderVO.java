package com.aacoptics.wlg.equipment.entity.vo;

import com.aacoptics.common.web.entity.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionSummaryOrderVO {


    /**
     * 工段类型
     */
    private String sectionType;

    /**
     * 需点检工单数
     */
    private Integer inspectionOrderCount;

    /**
     * 已提交点检工单数
     */
    private Integer committedInspectionOrderCount;

    /**
     * 已确认点检工单数
     */
    private Integer confirmedInspectionOrderCount;

    /**
     * 未完成点检工单数
     */
    private Integer unfinishedInspectionOrderCount;


    /**
     * 需保养工单数
     */
    private Integer maintenanceOrderCount;

    /**
     * 已提交保养工单数
     */
    private Integer committedMaintenanceOrderCount;

    /**
     * 已确认保养工单数
     */
    private Integer confirmedMaintenanceOrderCount;

    /**
     * 未完成保养工单数
     */
    private Integer unfinishedMaintenanceOrderCount;


    /**
     * 需维修工单数
     */
    private Integer repairOrderCount;

    /**
     * 已提交维修工单数
     */
    private Integer committedRepairOrderCount;

    /**
     * 已确认维修工单数
     */
    private Integer confirmedRepairOrderCount;

    /**
     * 未完成维修工单数
     */
    private Integer unfinishedRepairOrderCount;
}
