package com.aacoptics.fanuc.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_fanuc_check_data_every_day")
public class FanucCheckDataEveryDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String site;

    private String machineName;

    private String shiftDate;

    private String moldFileName;

    private Double monitCycle;

    private Integer monitCycleCount;

    private Double monitVPPrs;
    private Double monitVPPos;
    private Double monitRecovTime;
    private Double monitInjTime;
    private Double monitMCushion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dbCreateTime;
    private Double monitNozzle;

    @TableField("monit_barrel_1")
    private Double monitBarrel1;

    @TableField("monit_barrel_2")
    private Double monitBarrel2;

    @TableField("monit_barrel_3")
    private Double monitBarrel3;
    private Double monitFeedTh;
    private Double condAutoDieHForce;

    @TableField("cond_nozzle_1_set")
    private Double condNozzle1Set;
    @TableField("cond_barrel_1_set")
    private Double condBarrel1Set;
    @TableField("cond_barrel_2_set")
    private Double condBarrel2Set;
    @TableField("cond_barrel_3_set")
    private Double condBarrel3Set;
    private Double condFeedThroatSet;
    @TableField("cond_back_pres1")
    private Double condBackPres1;
    @TableField("cond_screw_rotate1")
    private Double condScrewRotate1;
    @TableField("cond_extrd_sw_pos_1")
    private Double condExtrdSwPos1;
    private Double condDcmpDist;
    private Double condDcmpVel;

    @TableField("cond_cool_time_1")
    private Double condCoolTime1;
    @TableField("cond_inj_speed_1")
    private Double condInjSpeed1;
    @TableField("cond_inj_speed_2")
    private Double condInjSpeed2;
    @TableField("cond_inj_speed_3")
    private Double condInjSpeed3;
    @TableField("cond_inj_speed_4")
    private Double condInjSpeed4;
    @TableField("cond_inj_speed_5")
    private Double condInjSpeed5;
    @TableField("cond_inj_switch_pos_1")
    private Double condInjSwitchPos1;
    @TableField("cond_inj_switch_pos_2")
    private Double condInjSwitchPos2;
    @TableField("cond_inj_switch_pos_3")
    private Double condInjSwitchPos3;
    @TableField("cond_inj_switch_pos_4")
    private Double condInjSwitchPos4;
    private String condInjectMode;
    private Double condTransPosition;
    @TableField("cond_pack_pres_1")
    private Double condPackPres1;
    @TableField("cond_pack_pres_2")
    private Double condPackPres2;
    @TableField("cond_pack_pres_3")
    private Double condPackPres3;
    @TableField("cond_pack_pres_4")
    private Double condPackPres4;
    @TableField("cond_pack_time_1")
    private Double condPackTime1;
    @TableField("cond_pack_time_2")
    private Double condPackTime2;
    @TableField("cond_pack_time_3")
    private Double condPackTime3;
    @TableField("cond_pack_time_4")
    private Double condPackTime4;


}
