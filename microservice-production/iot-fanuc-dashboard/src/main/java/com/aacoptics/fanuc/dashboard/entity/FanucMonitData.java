package com.aacoptics.fanuc.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_fanuc_monit_data")
public class FanucMonitData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String site;

    private String monitMcName;

    private Integer monitMcId;

    private String condMoldFileName;

    private String monitDate;

    private String monitTime;

    private LocalDateTime monitDateTime;

    private String monitStatus;

    private String monitCycle;

    private String monitInjTime;

    private String monitRecovTime;

    private String monitMCushion;

    private String monitExtrdPos;

    private String monitPeakPrs;

    private String monitShotcount;

    private String monitGoodcount;

    private String monitVPPos;

    private String monitMold1;

    private String monitMold2;

    private String monitNozzle;

    private String monitNozzle2;

    private String monitBarrel1;

    private String monitBarrel2;

    private String monitBarrel3;

    private String monitBarrel4;

    private String monitFeedTh;

    private String monitExtrdStart;

    private String monitExtrdTorq;

    private String monitMold3;

    private String monitMold4;

    private String monitPeakT;

    private String monitPeakPos;

    private String monitEjeDevStTrq;

    private String monitCloseTime;

    private String monitVPPrs;

    private String monitMold5;

    private String monitMold6;

    private String monitInjPres;

    private String monitVPAdj;

    private String monitFlwPeak;

    private String monitBackflw;

    private String monitLockupTim;

    private String monitPickupTim;

    private String monitResidenceT;

    private String monitEjeDevAvTrq;

    private String monitMold7;

    private String monitMold8;

    private String monitInjStartPos;

    private String monitScrewRevolution;

    private String monitCyclecount;

    @TableField("dbCreateTime")
    private LocalDateTime dbCreateTime;


}
