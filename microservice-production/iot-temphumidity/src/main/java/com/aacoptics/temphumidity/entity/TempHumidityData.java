package com.aacoptics.temphumidity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TempHumidityData implements Serializable {

    private String buildingNo;

    private String floorNo;

    private String meterNo;

    private BigDecimal temperature;

    private BigDecimal humidity;

    private String sensorNumber;

    private String workDate;

    private String collectTime;
}
