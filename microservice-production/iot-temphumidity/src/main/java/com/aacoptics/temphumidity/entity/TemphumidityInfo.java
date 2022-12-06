package com.aacoptics.temphumidity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TemphumidityInfo implements Serializable {
    private String sensorNumber;

    private String time;

    private String receiveDate;

    private String devTemplateUniqueId;

    private String deviceId;

    private String thatMomentProgramVersionId;

    private String temperature;

    private String humidity;

    private String dataColTime;

    private String clientId;

    private String cncStatus;
}
