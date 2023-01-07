package com.aacoptics.wlg.dashboard.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_wlg_molding_mk4_detail_data")
public class MoldingMK4Data implements Serializable {

    private String equipmentName;

    private String batchId;

    private String recipeName;

    private String waferType;

    private LocalDateTime logFileStartTime;

    private double ph1UpHeatingRateSetPoint;

    private double ph1LowHeatingRateSetPoint;

    private double ph1Position;

    private double ph1Pressure;

    private double ph2TempUpSetPoint;

    private double ph2TempLowSetPoint;

    private double ph2Position;

    private double ph2ForceMax;

    private double ph2ForceMin;

    private double ph3TempUpSetPoint;

    private double ph3TempLowSetPoint;

    private double ph3Pressure;

    private double ph3PumpTime;

    private double ph3TempUpActual;

    private double ph3TempLowActual;

    private double ph4Pressure;

    private double ph4TempUpSetPoint;

    private double ph4TempLowSetPoint;

    private double ph4TempUpActualMax;

    private double ph4TempUpActualMin;

    private double ph4TempLowActualMax;

    private double ph4TempLowActualMin;

    private double ph4SoakingTime;

    private double ph5Position;

    private double ph5ForceMax;

    private double ph5ForceMin;

    private double ph6TempUpSetPoint;

    private double ph6TempLowSetPoint;

    private double ph6FRaisingRate;

    private double ph7TempUpSetPoint;

    private double ph7TempLowSetPoint;

    private double ph7TempUpActualMax;

    private double ph7TempUpActualMin;

    private double ph7TempLowActualMax;

    private double ph7TempLowActualMin;

    private double ph7Force;

    private double ph7Position;

    private double ph7Pressure;

    private double ph7MoldingTime;

    private double ph8UpCoolingRateSetPoint;

    private double ph8LowCoolingRateSetPoint;

    private double ph8Force;

    private double ph8Pressure;

    private double ph8TempUpActual;

    private double ph8TempLowActual;

    private double ph8TempUpLow;

    private double ph9UpCoolingRateSetPoint;

    private double ph9LowCoolingRateSetPoint;

    private double ph9TempUpActual;

    private double ph9TempLowActual;

    private double ph9TempUpLow;

    private double ph10TempUpActual30N;

    private double ph10TempLowActual30N;

    private double ph10TempUpLow30N;

    private double ph10ForceNegative;

    private double ph10TimeNegative;

    private double ph10TempUpActualNegative;

    private double ph10TempLowActualNegative;

    private double ph10TempUpLowNegative;

    private double ph11Position;

    private double p12UpCoolingRateSetPoint;

    private double p12LowCoolingRateSetPoint;

    private double p12TempLowActual;

    private double ph12PickPlaceTempActual;

    private double ph12ExchangeTempActual;

    private double tempUpActualFp1;

    private double tempLowActualFp1;

    private double tempUpLowFp1;

    private double tempUpHeatBedAverageFp1;

    private double tempLowHeatBedAverageFp1;

    private double tempUpActualFp2;

    private double tempLowActualFp2;

    private double tempUpLowFp2;

    private double tempUpHeatBedAverageFp2;

    private double tempLowHeatBedAverageFp2;

    private double tempUpActualFp3;

    private double tempLowActualFp3;

    private double tempUpLowFp3;

    private double tempUpHeatBedAverageFp3;

    private double tempLowHeatBedAverageFp3;
}
