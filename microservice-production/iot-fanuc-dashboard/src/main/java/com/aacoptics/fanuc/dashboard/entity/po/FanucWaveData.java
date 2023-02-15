package com.aacoptics.fanuc.dashboard.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("Z_Fanuc_Special_Wave_Base_Info")
public class FanucWaveData implements Serializable {

    private String machineNo;

    private Integer cycleCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    private Integer timeStamp;

    @TableField(value = "inject_pressure")
    private float injectPressure;

    @TableField(value = "analog_input_1")
    private float analogInput1;
}
