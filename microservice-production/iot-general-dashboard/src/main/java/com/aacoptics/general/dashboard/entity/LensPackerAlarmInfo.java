package com.aacoptics.general.dashboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LensPackerAlarmInfo implements Serializable {
    private String alarmCode;
    private LocalDateTime startTime;
    private Boolean alarmData;
    private String machineNo;
    private String site;
    private Integer cavityNums;
    private String alarmDesc;
    private Long duration;
    private String machineType;

}
