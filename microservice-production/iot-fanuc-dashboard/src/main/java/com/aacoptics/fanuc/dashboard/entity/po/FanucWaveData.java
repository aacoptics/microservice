package com.aacoptics.fanuc.dashboard.entity.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
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

    @Excel(name = "机台号", orderNum = "1")
    private String machineNo;

    @Excel(name = "模次号", orderNum = "2")
    private Integer cycleCount;

    @Excel(name = "模次开始时间", orderNum = "3")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Excel(name = "时间戳（毫秒）", orderNum = "4")
    private Integer timeStamp;

    @Excel(name = "射出压", orderNum = "5")
    @TableField(value = "inject_pressure")
    private float injectPressure;

    @Excel(name = "喷嘴压", orderNum = "6")
    @TableField(value = "analog_input_1")
    private float analogInput1;
}
