package com.aacoptics.fanuc.dashboard.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_fanuc_mold_data_every_day")
public class FanucEnergyData implements Serializable {

    private String moldStartDate;

    private BigDecimal energy;
}
