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
    private LocalDateTime dbCreateTime;
    private Double monitNozzle;
    private Double monitBarrel1;
    private Double monitBarrel2;
    private Double monitBarrel3;
    private Double monitFeedTh;
    private Double condAutoDieHForce;
    private Double condNozzle1Set;
    private Double condBarrel1Set;
    private Double condBarrel2Set;
    private Double condBarrel3Set;
    private Double condFeedThroatSet;
    private Double condBackPres1;
    private Double condScrewRotate1;
    private Double condExtrdSwPos1;
    private Double condDcmpDist;
    private Double condDcmpVel;
    private Double condCoolTime1;
    private Double condInjSpeed1;
    private Double condInjSpeed2;
    private Double condInjSpeed3;
    private Double condInjSpeed4;
    private Double condInjSpeed5;
    private Double condInjSwitchPos1;
    private Double condInjSwitchPos2;
    private Double condInjSwitchPos3;
    private Double condInjSwitchPos4;
    private String condInjectMode;
    private Double condTransPosition;
    private Double condPackPres1;
    private Double condPackPres2;
    private Double condPackPres3;
    private Double condPackPres4;
    private Double condPackTime1;
    private Double condPackTime2;
    private Double condPackTime3;
    private Double condPackTime4;

}
