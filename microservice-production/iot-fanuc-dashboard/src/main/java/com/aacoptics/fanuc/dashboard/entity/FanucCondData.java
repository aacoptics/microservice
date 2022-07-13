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
@TableName("z_fanuc_cond_data")
public class FanucCondData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String site;

    private String monitMcName;

    private Integer monitMcId;

    private String condMoldFileName;

    private String condMoldId;

    private String condPackStep;

    private String condPackPres1;

    private String condPackPres2;

    private String condPackPres3;

    private String condPackPres4;

    private String condPackPres5;

    private String condPackPres6;

    private String condBefExtPres;

    private String condPackTime1;

    private String condPackTime2;

    private String condPackTime3;

    private String condPackTime4;

    private String condPackTime5;

    private String condPackTime6;

    private String condBefExtTime;

    private String condMaxInjPres;

    private String condMaxInjTime;

    private String condMaxPackVel;

    private String condAccelTime;

    private String condAcceleration;

    private String condShotSize;

    private String condInjStep;

    private String condInjSpeed1;

    private String condInjSpeed2;

    private String condInjSpeed3;

    private String condInjSpeed4;

    private String condInjSpeed5;

    private String condInjSpeed6;

    private String condPurgePressure;

    private String condPurgeRotation;

    private String condAutoDieHForce;

    private String condInjSpeed7;

    private String condInjSpeed8;

    private String condInjSpeed9;

    private String condInjSpeed10;

    private String condMaxInjectPress1;

    private String condMaxInjectPress2;

    private String condMaxInjectPress3;

    private String condMaxInjectPress4;

    private String condMaxInjectPress5;

    private String condMaxInjectPress6;

    private String condMaxInjectPress7;

    private String condMaxInjectPress8;

    private String condMaxInjectPress9;

    private String condMaxInjectPress10;

    private String condInjSwitchPos1;

    private String condInjSwitchPos2;

    private String condInjSwitchPos3;

    private String condInjSwitchPos4;

    private String condInjSwitchPos5;

    private String condInjSwitchPos6;

    private String condInjSwitchPos7;

    private String condInjSwitchPos8;

    private String condInjSwitchPos9;

    private String condInjectMode;

    private String condTransPosition;

    private String condTransPressure;

    private String condTransPressStep;

    private String condTransCavNzlPrs;

    private String condTransCavityStep;

    private String condTransNozzlePrs;

    private String condTransNozzleStep;

    private String condSgnlTransfStep;

    private String condExtrdStep;

    private String condBackPres1;

    private String condBackPres2;

    private String condBackPres3;

    private String condBackPres4;

    private String condBackPres5;

    private String condBackPres6;

    private String condScrewRotate1;

    private String condScrewRotate2;

    private String condScrewRotate3;

    private String condScrewRotate4;

    private String condScrewRotate5;

    private String condScrewRotate6;

    private String condNzl1HoldTemp;

    private String condNzl2HoldTemp;

    private String condNzlAdaptHoldTemp;

    private String condBrl1HoldTemp;

    private String condBrl2HoldTemp;

    private String condBrl3HoldTemp;

    private String condBrl4HoldTemp;

    private String condBrl5HoldTemp;

    private String condBrl6HoldTemp;

    private String condNozzle1Set;

    private String condBarrel1Set;

    private String condBarrel2Set;

    private String condBarrel3Set;

    private String condBarrel4Set;

    private String condBarrel5Set;

    private String condBarrel6Set;

    private String condMold1Set;

    private String condMold2Set;

    private String condEjectStartModeMold;

    private String condEjectStartPosMold;

    private String condAccelMode;

    private String condExtrdSw;

    @TableField("cond_extrd_sw_pos_1")
    private String condExtrdSwPos1;

    @TableField("cond_extrd_sw_pos_2")
    private String condExtrdSwPos2;

    @TableField("cond_extrd_sw_pos_3")
    private String condExtrdSwPos3;

    @TableField("cond_extrd_sw_pos_4")
    private String condExtrdSwPos4;

    @TableField("cond_extrd_sw_pos_5")
    private String condExtrdSwPos5;

    private String condDcmpDist;

    private String condDcmpVel;

    private String condCoolTime1;

    private String condCoolTime2;

    private String condNozzle1High;

    private String condBarrel1High;

    private String condBarrel2High;

    private String condBarrel3High;

    private String condFeedThroatHigh;

    private String condMold1High;

    private String condMold2High;

    private String condMold3High;

    private String condMold4High;

    private String condFeedThroatSet;

    private String condMold3Set;

    private String condMold4Set;

    private String condNozzle1Low;

    private String condBarrel1Low;

    private String condBarrel2Low;

    private String condBarrel3Low;

    private String condFeedThroatLow;

    private String condMold1Low;

    private String condMold2Low;

    private String condMold3Low;

    private String condMold4Low;

    private String condCloseLimitPos;

    @TableField("cond_close_sw_pos_1")
    private String condCloseSwPos1;

    @TableField("cond_close_sw_pos_2")
    private String condCloseSwPos2;

    @TableField("cond_close_sw_pos_3")
    private String condCloseSwPos3;

    private String condMoldProtect;

    private String condMoldTouchPos;

    @TableField("cond_open_sw_pos_1")
    private String condOpenSwPos1;

    @TableField("cond_open_sw_pos_2")
    private String condOpenSwPos2;

    @TableField("cond_open_sw_pos_3")
    private String condOpenSwPos3;

    @TableField("cond_open_sw_pos_4")
    private String condOpenSwPos4;

    private String condFullyOpenPos;

    private String condEjectStartPos;

    @TableField("cond_close_vel_1")
    private String condCloseVel1;

    @TableField("cond_close_vel_2")
    private String condCloseVel2;

    @TableField("cond_close_vel_3")
    private String condCloseVel3;

    @TableField("cond_close_vel_4")
    private String condCloseVel4;

    private String condMoldTouchVel;

    @TableField("cond_open_vel_1")
    private String condOpenVel1;

    @TableField("cond_open_vel_2")
    private String condOpenVel2;

    @TableField("cond_open_vel_3")
    private String condOpenVel3;

    @TableField("cond_open_vel_4")
    private String condOpenVel4;

    private String condFullyOpenVel;

    private String condCloseStep;

    private String condOpenStep;

    @TableField("cond_mold_protect_1")
    private String condMoldProtect1;

    @TableField("cond_mold_protect_1_minus")
    private String condMoldProtect1Minus;

    @TableField("cond_mold_protect_2")
    private String condMoldProtect2;

    @TableField("cond_mold_protect_2_minus")
    private String condMoldProtect2Minus;

    @TableField("cond_protect_time_1")
    private String condProtectTime1;

    @TableField("cond_protect_time_2")
    private String condProtectTime2;

    private String condEjectPattern1;

    private String condEjectPulse;

    private String condEjectStartMode;

    private String condEjectRetractPos;

    private String condEjectFullyAdvance;

    private String condEjectRetractVel;

    private String condEjectAdvanceVel;

    private String condEjectPattern2;

    private String condEjectDwellInRet;

    private String condEjectDwellInAdv;

    private String condHrMode;

    private String condTransMode;

    @TableField("cond_proc_moni_item_5")
    private String condProcMoniItem5;

    @TableField("dbCreateTime")
    private LocalDateTime dbCreateTime;


}
