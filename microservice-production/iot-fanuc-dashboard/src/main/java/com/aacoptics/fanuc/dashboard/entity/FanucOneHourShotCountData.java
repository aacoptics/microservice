package com.aacoptics.fanuc.dashboard.entity;

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
@TableName("z_fanuc_one_hour_shotcount")
public class FanucOneHourShotCountData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String monitMcName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer shotCount;
}